package com.pichillilorenzo.flutter_inappwebview.tracing;

import androidx.annotation.Nullable;
import androidx.webkit.TracingConfig;
import androidx.webkit.TracingController;
import androidx.webkit.WebViewFeature;

import com.pichillilorenzo.flutter_inappwebview.InAppWebViewFlutterPlugin;
import com.pichillilorenzo.flutter_inappwebview.types.Disposable;

import io.flutter.plugin.common.MethodChannel;

public class TracingControllerManager implements Disposable {
  protected static final String LOG_TAG = "TracingControllerMan";
  public static final String METHOD_CHANNEL_NAME = "com.pichillilorenzo/flutter_inappwebview_tracingcontroller";

  @Nullable
  public TracingControllerChannelDelegate channelDelegate;
  @Nullable
  public static TracingController tracingController;
  @Nullable
  public InAppWebViewFlutterPlugin plugin;

  public TracingControllerManager(final InAppWebViewFlutterPlugin plugin) {
    this.plugin = plugin;
    final MethodChannel channel = new MethodChannel(plugin.messenger, METHOD_CHANNEL_NAME);
    this.channelDelegate = new TracingControllerChannelDelegate(this, channel);
    if (WebViewFeature.isFeatureSupported(WebViewFeature.TRACING_CONTROLLER_BASIC_USAGE)) {
      tracingController = TracingController.getInstance();
    } else {
      tracingController = null;
    }
  }

  public static TracingConfig buildTracingConfig(TracingSettings settings) {
    TracingConfig.Builder builder = new TracingConfig.Builder();
    for (Object category : settings.categories) {
      if (category instanceof String) {
        builder.addCategories((String) category);
      }
      if (category instanceof Integer) {
        builder.addCategories((Integer) category);
      }
    }
    if (settings.tracingMode != null) {
      builder.setTracingMode(settings.tracingMode);
    }
    return builder.build();
  }

  @Override
  public void dispose() {
    if (channelDelegate != null) {
      channelDelegate.dispose();
      channelDelegate = null;
    }
    tracingController = null;
    plugin = null;
  }
}
