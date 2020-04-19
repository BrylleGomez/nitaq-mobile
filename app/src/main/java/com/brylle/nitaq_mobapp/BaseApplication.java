//package com.brylle.nitaq_mobapp;
//
//import android.app.Activity;
//import android.app.Application;
//import android.os.Bundle;
//
//import java.lang.ref.WeakReference;
//
//public class  BaseApplication extends Application {
//
//    public interface LifecycleDelegate {
//
//        void onApplicationStart(Application app);
//        void onApplicationStop(Application app);
//    }
//
//    private boolean isRunningForeground = false;
//    private WeakReference<LifecycleDelegate> lifecycleDelegate;
//
//    @Override
//    public void onCreate() {
//
//        super.onCreate();
//
//        final Application thisApp = this;
//
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//
//            @Override
//            public void onActivityResumed(Activity activity) {
//
//                boolean wasRunningForeground = getRunningForeground();
//
//                setRunningForeground(true);
//
//                if (!wasRunningForeground) {
//                    if (getLifecycleDelegate() != null) {
//                        getLifecycleDelegate().onApplicationStart(thisApp);
//                    }
//                }
//            }
//
//            @Override
//            public void onActivityPaused(Activity activity) {
//
//                boolean wasRunningForeground = getRunningForeground();
//
//                setRunningForeground(false);
//
//                if (wasRunningForeground) {
//                    if (getLifecycleDelegate() != null) {
//                        getLifecycleDelegate().onApplicationStop(thisApp);
//                    }
//                }
//            }
//
//            @Override
//            public void onActivityCreated(Activity activity, Bundle bundle) {
//            }
//
//            @Override
//            public void onActivityStarted(Activity activity) {
//
//            }
//
//            @Override
//            public void onActivityStopped(Activity activity) {
//            }
//
//            @Override
//            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//            }
//
//            @Override
//            public void onActivityDestroyed(Activity activity) {
//            }
//        });
//    }
//
//    private void setRunningForeground(boolean isRunningForeground) {
//        this.isRunningForeground = isRunningForeground;
//    }
//
//    private boolean getRunningForeground() {
//        return this.isRunningForeground;
//    }
//
//    public synchronized void setLifecyleDelegate(LifecycleDelegate lifecycleDelegate) {
//        this.lifecycleDelegate = new WeakReference<>(lifecycleDelegate);
//    }
//
//    private synchronized LifecycleDelegate getLifecycleDelegate() {
//        return this.lifecycleDelegate != null ? this.lifecycleDelegate.get() : null;
//    }
//}
//
