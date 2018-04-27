LOCAL_PATH := $(call my-dir)
$(warning LOCAL_PATH:$(LOCAL_PATH))

include $(CLEAR_VARS)

LOCAL_MODULE := apollo-prebuild
LOCAL_SRC_FILES := $(abspath $(LOCAL_PATH)/../libtdir.so)

LOCAL_EXPORT_C_INCLUDES := $(abspath $(LOCAL_PATH)/../header/include/tdir)

$(warning EXPORT: $(LOCAL_EXPORT_C_INCLUDES))

include $(PREBUILT_SHARED_LIBRARY)
