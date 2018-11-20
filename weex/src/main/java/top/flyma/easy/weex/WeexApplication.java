package top.flyma.easy.weex;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.alibaba.android.bindingx.plugin.weex.BindingX;
import com.alibaba.weex.commons.adapter.DefaultWebSocketAdapterFactory;
import com.alibaba.weex.commons.adapter.ImageAdapter;
import com.alibaba.weex.commons.adapter.JSExceptionAdapter;
import com.alibaba.weex.extend.adapter.DefaultAccessibilityRoleAdapter;
import com.alibaba.weex.extend.adapter.InterceptWXHttpAdapter;
import com.alibaba.weex.extend.component.RichText;
import com.alibaba.weex.extend.component.WXComponentSyncTest;
import com.alibaba.weex.extend.component.WXMask;
import com.alibaba.weex.extend.component.WXParallax;
import com.alibaba.weex.extend.module.*;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.WXException;
import top.flyma.easy.weex.extend.module.NativeModule;

import java.util.Locale;

public class WeexApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDebugEnvironment(true, false, "10.10.11.104");
        WXBridgeManager.updateGlobalConfig("wson_on");
        WXEnvironment.setOpenDebugLog(true);
        WXEnvironment.setApkDebugable(true);
        WXSDKEngine.addCustomOptions("appName", "easy-weex");
        WXSDKEngine.addCustomOptions("appGroup", "top.flyme.easy");

        WXSDKEngine.initialize(this,
                new InitConfig.Builder()
                        //.setImgAdapter(new FrescoImageAdapter())// use fresco adapter
                        .setImgAdapter(new ImageAdapter())
                        .setWebSocketAdapterFactory(new DefaultWebSocketAdapterFactory())
                        .setJSExceptionAdapter(new JSExceptionAdapter())
                        .setHttpAdapter(new InterceptWXHttpAdapter())
//                        .setApmGenerater(new ApmGenerator())
                        .build()
        );
        Log.d("WEEX", "init");

        WXSDKManager.getInstance().setAccessibilityRoleAdapter(new DefaultAccessibilityRoleAdapter());

        try {
            Fresco.initialize(this);
            WXSDKEngine.registerComponent("synccomponent", WXComponentSyncTest.class);
            WXSDKEngine.registerComponent(WXParallax.PARALLAX, WXParallax.class);

            WXSDKEngine.registerComponent("richtext", RichText.class);
            WXSDKEngine.registerModule("render", RenderModule.class);
            WXSDKEngine.registerModule("event", WXEventModule.class);
            WXSDKEngine.registerModule("syncTest", SyncTestModule.class);

            WXSDKEngine.registerComponent("mask",WXMask.class);
            WXSDKEngine.registerModule("myModule", MyModule.class);
            WXSDKEngine.registerModule("geolocation", GeolocationModule.class);

            WXSDKEngine.registerModule("titleBar", WXTitleBar.class);

            WXSDKEngine.registerModule("wsonTest", WXWsonTestModule.class);
            WXSDKEngine.registerModule("navigator", LocaleNavigatorModule.class);
            WXSDKEngine.registerModule("native", NativeModule.class);

            loadPlugins();
            /**
             * override default image tag
             * WXSDKEngine.registerComponent("image", FrescoImageComponent.class);
             */

            //Typeface nativeFont = Typeface.createFromAsset(getAssets(), "font/native_font.ttf");
            //WXEnvironment.setGlobalFontFamily("bolezhusun", nativeFont);

        } catch (WXException e) {
            e.printStackTrace();
        }

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // The demo code of calling 'notifyTrimMemory()'
                if (false) {
                    // We assume that the application is on an idle time.
                    WXSDKManager.getInstance().notifyTrimMemory();
                }
                // The demo code of calling 'notifySerializeCodeCache()'
                if (false) {
                    WXSDKManager.getInstance().notifySerializeCodeCache();
                }
            }
        });
    }

    /**
     * 加载插件
     * @throws WXException
     */
    private void loadPlugins() throws WXException {
        BindingX.register();
    }

    /**
     *@param connectable debug server is connectable or not.
     *               if true, sdk will try to connect remote debug server when init WXBridge.
     *
     * @param debuggable enable remote debugger. valid only if host not to be "DEBUG_SERVER_HOST".
     *               true, you can launch a remote debugger and inspector both.
     *               false, you can  just launch a inspector.
     * @param host the debug server host, must not be "DEBUG_SERVER_HOST", a ip address or domain will be OK.
     *             for example "127.0.0.1".
     */
    private void initDebugEnvironment(boolean connectable, boolean debuggable, String host) {
        if (!"DEBUG_SERVER_HOST".equals(host)) {
            WXEnvironment.sDebugServerConnectable = connectable;
            WXEnvironment.sRemoteDebugMode = debuggable;
            WXEnvironment.sRemoteDebugProxyUrl = "ws://" + host + ":8091/debugProxy/native";
        }
    }
}