package com.humminbird.machinepartverifierandroid.Utilities;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.humminbird.machinepartverifierandroid.R;

public class AnimationAssistant {
    private Context context;
    private Animation entrance_left;
    private Animation entrance_right;
    private Animation fade_in;
    private Animation fade_out;
    private Animation grow_in;
    private Animation rotate_in;
    private Animation rotate_out;
    private Animation toast_in;
    private Animation toast_out;
    private Animation shrink_out;
    private Animation bloat;
    private Animation normalize;

    public enum AnimationGroup{
        Entrance,
        Grow_Shrink,
        Toast,
        Rotate,
        Fade
    }

    public enum AnimationPhase{
        In,
        Out,
        Left,
        Right,
        Grow,
        Shrink
    }

    private Context getContext() {
        return context;
    }

    public AnimationAssistant(Context context){
        this.context = context;
    }

    private void smartInit(AnimationGroup group, Runnable afterInit){
        switch(group){
            case Entrance:
                entrance_left = AnimationUtils.loadAnimation(getContext(), R.anim.entrance_left);
                entrance_right = AnimationUtils.loadAnimation(getContext(), R.anim.entrance_right);
                if(grow_in == null){
                    grow_in = AnimationUtils.loadAnimation(getContext(),R.anim.grow_in);
                }
                break;
            case Fade:
                fade_out = AnimationUtils.loadAnimation(getContext(),R.anim.fade_out);
                fade_in = AnimationUtils.loadAnimation(getContext(),R.anim.fade_in);
                break;
            case Toast:
                toast_in = AnimationUtils.loadAnimation(getContext(),R.anim.toast_in);
                toast_out = AnimationUtils.loadAnimation(getContext(),R.anim.toast_out);
                break;
            case Grow_Shrink:
                if(grow_in == null){
                    grow_in = AnimationUtils.loadAnimation(getContext(),R.anim.grow_in);
                }

                shrink_out = AnimationUtils.loadAnimation(getContext(),R.anim.shrink_out);
                bloat = AnimationUtils.loadAnimation(getContext(),R.anim.bloat);
                normalize = AnimationUtils.loadAnimation(getContext(),R.anim.normalize);
                break;
            case Rotate:
                rotate_in = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_in);
                rotate_out = AnimationUtils.loadAnimation(getContext(),R.anim.rotate_out);
                break;

                default:
                    break;
        }
    }

    public void animateView(final View view, AnimationGroup animation, AnimationPhase requestedPhase) throws IllegalArgumentException{
        switch (animation){
            case Fade:
                switch (requestedPhase){
                    case In:
                        if(fade_in == null){
                            smartInit(AnimationGroup.Fade, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(fade_in);
                                }
                            });
                        } else {
                            view.startAnimation(fade_in);
                        }
                        break;
                    case Out:
                        if(fade_out == null){
                            smartInit(AnimationGroup.Fade, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(fade_out);
                                }
                            });
                        } else {
                            view.startAnimation(fade_out);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the Fade animation group.");
                }
                break;


            case Toast:
                switch (requestedPhase){
                    case In:
                        if(toast_in == null){
                            smartInit(AnimationGroup.Toast, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(toast_in);
                                }
                            });
                        } else {
                            view.startAnimation(toast_in);
                        }
                        break;
                    case Out:
                        if(toast_out == null){
                            smartInit(AnimationGroup.Toast, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(toast_out);
                                }
                            });
                        } else {
                            view.startAnimation(toast_out);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group.");
                }
                break;


            case Entrance:
                switch (requestedPhase){
                    case Left:
                        if(entrance_left == null){
                            smartInit(AnimationGroup.Entrance, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(entrance_left);
                                }
                            });
                        } else {
                            view.startAnimation(entrance_left);
                        }
                        break;
                    case Right:
                        if(entrance_right == null){
                            smartInit(AnimationGroup.Entrance, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(entrance_right);
                                }
                            });
                        } else {
                            view.startAnimation(entrance_right);
                        }
                    case Grow:
                        if(grow_in == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(grow_in);
                                }
                            });
                        } else {
                            view.startAnimation(grow_in);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group.");
                }
                break;

            case Rotate:
                switch (requestedPhase){
                    case In:
                        if(rotate_in == null){
                            smartInit(AnimationGroup.Rotate, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(rotate_in);
                                }
                            });
                        } else {
                            view.startAnimation(rotate_in);
                        }
                        break;
                    case Out:
                        if(rotate_out == null){
                            smartInit(AnimationGroup.Rotate, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(rotate_out);
                                }
                            });
                        } else {
                            view.startAnimation(rotate_out);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group yet.");
                }
                break;


            case Grow_Shrink:
                switch (requestedPhase){
                    case Grow:
                        //view.startAnimation(rotate_in);
                        if(bloat == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(bloat);
                                }
                            });
                        } else {
                            view.startAnimation(bloat);
                        }
                        break;
                    case Shrink:
                        //view.startAnimation(rotate_out);
                        if(normalize == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(normalize);
                                }
                            });
                        } else {
                            view.startAnimation(normalize);
                        }
                        break;
                    case In:
                        if(grow_in == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(grow_in);
                                }
                            });
                        } else {
                            view.startAnimation(grow_in);
                        }
                        break;
                    case Out:
                        if(shrink_out == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(shrink_out);
                                }
                            });
                        } else {
                            view.startAnimation(shrink_out);
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group yet.");
                }
                break;
        }
    }

    public void animateView(final View view, AnimationGroup animation, AnimationPhase requestedPhase, final Runnable postAnimation) throws IllegalArgumentException{
        switch (animation){
            case Fade:
                switch (requestedPhase){
                    case In:
                        if(fade_in == null){
                            smartInit(AnimationGroup.Fade, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(fade_in);
                                    view.postDelayed(postAnimation,fade_in.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(fade_in);
                            view.postDelayed(postAnimation,fade_in.getDuration());
                        }
                        break;
                    case Out:
                        if(fade_out == null){
                            smartInit(AnimationGroup.Fade, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(fade_out);
                                    view.postDelayed(postAnimation,fade_out.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(fade_out);
                            view.postDelayed(postAnimation,fade_out.getDuration());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the Fade animation group.");
                }
                break;


            case Toast:
                switch (requestedPhase){
                    case In:
                        if(toast_in == null){
                            smartInit(AnimationGroup.Toast, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(toast_in);
                                    view.postDelayed(postAnimation,toast_in.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(toast_in);
                            view.postDelayed(postAnimation,toast_in.getDuration());
                        }
                        break;
                    case Out:
                        if(toast_out == null){
                            smartInit(AnimationGroup.Toast, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(toast_out);
                                    view.postDelayed(postAnimation,toast_out.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(toast_out);
                            view.postDelayed(postAnimation,toast_out.getDuration());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group.");
                }
                break;


            case Entrance:
                switch (requestedPhase){
                    case Left:
                        if(entrance_left == null){
                            smartInit(AnimationGroup.Entrance, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(entrance_left);
                                    view.postDelayed(postAnimation,entrance_left.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(entrance_left);
                            view.postDelayed(postAnimation,entrance_left.getDuration());
                        }
                        break;
                    case Right:
                        if(entrance_right == null){
                            smartInit(AnimationGroup.Entrance, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(entrance_right);
                                    view.postDelayed(postAnimation,entrance_right.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(entrance_right);
                            view.postDelayed(postAnimation,entrance_right.getDuration());
                        }
                    case Grow:
                        if(grow_in == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(grow_in);
                                    view.postDelayed(postAnimation,grow_in.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(grow_in);
                            view.postDelayed(postAnimation,grow_in.getDuration());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group.");
                }
                break;

            case Rotate:
                switch (requestedPhase){
                    case In:
                        if(rotate_in == null){
                            smartInit(AnimationGroup.Rotate, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(rotate_in);
                                    view.postDelayed(postAnimation,rotate_in.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(rotate_in);
                            view.postDelayed(postAnimation,rotate_in.getDuration());
                        }
                        break;
                    case Out:
                        if(rotate_out == null){
                            smartInit(AnimationGroup.Rotate, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(rotate_out);
                                    view.postDelayed(postAnimation,rotate_out.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(rotate_out);
                            view.postDelayed(postAnimation,rotate_out.getDuration());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group yet.");
                }
                break;


            case Grow_Shrink:
                switch (requestedPhase){
                    case Grow:
                        //view.startAnimation(rotate_in);
                        if(bloat == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(bloat);
                                    view.postDelayed(postAnimation,bloat.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(bloat);
                            view.postDelayed(postAnimation,bloat.getDuration());
                        }
                        break;
                    case Shrink:
                        //view.startAnimation(rotate_out);
                        if(normalize == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(normalize);
                                    view.postDelayed(postAnimation,normalize.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(normalize);
                            view.postDelayed(postAnimation,normalize.getDuration());
                        }
                        break;
                    case In:
                        if(grow_in == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(grow_in);
                                    view.postDelayed(postAnimation,grow_in.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(grow_in);
                            view.postDelayed(postAnimation,grow_in.getDuration());
                        }
                        break;
                    case Out:
                        if(shrink_out == null){
                            smartInit(AnimationGroup.Grow_Shrink, new Runnable() {
                                @Override
                                public void run() {
                                    view.startAnimation(shrink_out);
                                    view.postDelayed(postAnimation,shrink_out.getDuration());
                                }
                            });
                        } else {
                            view.startAnimation(shrink_out);
                            view.postDelayed(postAnimation,shrink_out.getDuration());
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("The requested animation phase is not a member of the "+requestedPhase.toString()+" animation group yet.");
                }
                break;
        }
    }

}
