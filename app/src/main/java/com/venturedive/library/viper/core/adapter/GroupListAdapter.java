package com.venturedive.library.viper.core.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.venturedive.library.viper.R;
import com.venturedive.library.viper.util.ViewParamsUtils;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
abstract class GroupListAdapter<VH extends GroupListAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final Context context;
    private final RecyclerView recyclerView;
    boolean animateSummaryView = true;

    private final ArrayList<ViewParams> viewSet;
    private final LinearLayoutManager layoutManager;
    private ViewParams expandedItem;

    protected abstract void onBindExpandableViewHolder(VH holder, int position);
    protected abstract void onItemExpanded(VH view);
    protected abstract void onItemCollapsed(VH view);

    GroupListAdapter(Context context, int dataSetSize, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        viewSet = new ArrayList<>();
        for (int i = 0; i < dataSetSize; i++) {
            viewSet.add(new ViewParams(i));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindExpandableViewHolder(holder, position);
        resetItemView(holder, position);
    }

    public boolean isExpanded(int position) {
        return viewSet.get(position).isExpanded();
    }

    private VH getExpanded() {
        return expandedItem != null ? (VH) expandedItem.viewHolder : null;
    }

    public VH collapseExpandedItem() {
        if (expandedItem != null) {
            expandedItem.setExpanded(false);
            return getExpanded();
        }
        return null;
    }

    private void resetItemView(VH holder, int position) {
        ViewParams viewParams = viewSet.get(position);
        //noinspection unchecked
        viewParams.setDimensions(holder);
        viewParams.resetExpanded(holder);
    }

    private void onItemClick(VH holder, int position) {
        ViewParams viewParams = viewSet.get(position);
        viewParams.toggleExpanded(holder);
        if (viewParams.isExpanded())
            onItemExpanded(holder);
        else
            onItemCollapsed(holder);
    }

    class ViewParams {
        static final int ANIM_DURATION_EXPAND = 300;
        static final int ANIM_DURATION_IN = 200;

        boolean isChildViewExpanded = false;
        int parentViewHeight;
        int childViewHeight;
        int childViewWidth;
        int indicatorViewWidth;
        int summaryViewWidth;
        int position;
        ViewHolder viewHolder;

        ViewParams(int position) {
            this.position = position;
        }

        void setDimensions(ViewHolder holder) {
            holder.parentHolderView.measure(0, 0);
            holder.indicatorView.measure(0, 0);
            holder.summaryView.measure(0, 0);
            holder.childHolderView.measure(0, 0);
            parentViewHeight = holder.parentHolderView.getMeasuredHeight();
            indicatorViewWidth = holder.indicatorView.getMeasuredWidth();
            summaryViewWidth = holder.summaryView.getMeasuredWidth();
            if (childViewHeight <= 0) {
                childViewHeight = holder.childHolderView.getMeasuredHeight();
                childViewWidth = holder.childHolderView.getMeasuredWidth();
            }
        }

        boolean isExpanded() {
            return isChildViewExpanded;
        }

        void setExpanded(boolean expanded) {
            isChildViewExpanded = expanded;
            if (viewHolder != null) {
                if (position >= layoutManager.findFirstVisibleItemPosition() && position <= layoutManager.findLastVisibleItemPosition()) {
                    resetExpandedWithAnimation(viewHolder);
                } else
                    resetExpanded(viewHolder);
            }
        }

        void toggleExpanded(final ViewHolder holder) {
            isChildViewExpanded = !isChildViewExpanded;
            if (isChildViewExpanded) {
                viewHolder = holder;
                expandedItem = ViewParams.this;
                resetExpandedWithAnimation(holder);

            } else {
                viewHolder = null;
                expandedItem = null;
                resetExpandedWithAnimation(holder);
            }
        }

        void resetExpandedWithAnimation(ViewHolder holder) {
            expandSubItemWithAnimation(0f, holder);
            animateSubItemIn(holder);
        }

        void resetExpanded(ViewHolder holder) {
            if (isChildViewExpanded) {
                ViewParamsUtils.setViewHeight(holder.childHolderView, childViewHeight);
                holder.summaryView.setAlpha(0f);
            } else {
                ViewParamsUtils.setViewHeight(holder.childHolderView, 0);
                holder.summaryView.setAlpha(1f);
            }
        }

        /**
         * Method to adjust Item position in parent if its sub items are outside screen.
         */
        private void adjustItemPosIfHidden(ViewHolder holder) {
            int parentHeight = recyclerView.getMeasuredHeight();
            int[] parentPos = new int[2];
            recyclerView.getLocationOnScreen(parentPos);
            int parentY = parentPos[1];
            int[] itemPos = new int[2];
            holder.parentHolderView.getLocationOnScreen(itemPos);
            int itemY = itemPos[1];

            int endPosition = itemY + parentViewHeight + childViewHeight;
            int parentEnd = parentY + parentHeight;
            if (endPosition > parentEnd) {
                int delta = endPosition - parentEnd;
                final int itemDeltaToTop = itemY - parentY;

                holder.childHolderView.post(() -> recyclerView.smoothScrollBy(0, recyclerView.getScrollY() + itemDeltaToTop));
            }
        }

        private void expandSubItemWithAnimation(float startingPos, final ViewHolder holder) {
            if (holder.childHolderView != null) {
                final int totalHeight = childViewHeight;
                ValueAnimator animation = isChildViewExpanded ?
                        ValueAnimator.ofFloat(startingPos, totalHeight) :
                        ValueAnimator.ofFloat(totalHeight, startingPos);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.setDuration(ANIM_DURATION_EXPAND);

                animation.addUpdateListener(animation1 -> {
                    float val = (float) animation1.getAnimatedValue();
                    ViewParamsUtils.setViewHeight(holder.childHolderView, (int) val);
                });

                animation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        if (isChildViewExpanded) {
                            adjustItemPosIfHidden(holder);
                        }
                    }
                });

                animation.start();
            }
        }

        private void animateSubItemIn(ViewHolder holder) {
//            animateView(holder.childHolderView, 0, childViewWidth, isChildViewExpanded);
            animateViewAlpha(holder.childHolderView, isChildViewExpanded);
            if (animateSummaryView) {
                animateView(holder.summaryView, indicatorViewWidth, summaryViewWidth, !isChildViewExpanded);
                animateViewAlpha(holder.summaryView, !isChildViewExpanded);
            }
        }

        private void animateView(final View view, final int indicatorViewWidth, final int viewWidth, boolean isViewShown) {
            if (view == null) {
                return;
            }
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            ValueAnimator animation = isViewShown ?
                    ValueAnimator.ofFloat(0f, 1f) :
                    ValueAnimator.ofFloat(1f, 0f);
            animation.setDuration(ANIM_DURATION_IN);
            animation.setStartDelay(ANIM_DURATION_IN / 2);

            animation.addUpdateListener(animation1 -> {
                float val = (float) animation1.getAnimatedValue();
                view.setX(indicatorViewWidth + (viewWidth / 2 * val) - viewWidth / 2);
            });

            animation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            });

            animation.start();
        }

        private void animateViewAlpha(final View view, boolean isViewShown) {
            if (view == null) {
                return;
            }
            ValueAnimator animation = isViewShown ?
                    ValueAnimator.ofFloat(0f, 1f) :
                    ValueAnimator.ofFloat(1f, 0f);
            animation.setDuration(isViewShown ? ANIM_DURATION_IN * 2 : ANIM_DURATION_IN);
            animation.setStartDelay(isViewShown ? ANIM_DURATION_IN / 2 : 0);

            animation.addUpdateListener(animation1 -> {
                float val = (float) animation1.getAnimatedValue();
                view.setAlpha(val);
            });

            animation.start();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final LinearLayout parentHolderView;
        final LinearLayout childHolderView;
        final View indicatorView;
        final View summaryView;

        @SuppressWarnings("unchecked")
        public ViewHolder(ViewGroup parent, int mainLayout, int subLayout, int indicatorId, int summaryId) {
            super(LayoutInflater.from(context).inflate(R.layout.movie_layout, parent, false));
            parentHolderView = itemView.findViewById(R.id.lyt_parentView);
            childHolderView = itemView.findViewById(R.id.lyt_childView);
            parentHolderView.addView(LayoutInflater.from(context).inflate(mainLayout, parentHolderView, false));
            childHolderView.addView(LayoutInflater.from(context).inflate(subLayout, childHolderView, false));
            indicatorView = itemView.findViewById(indicatorId);
            summaryView = itemView.findViewById(summaryId);

            itemView.setOnClickListener(v -> onItemClick((VH) this, getAdapterPosition()));
        }
    }
}