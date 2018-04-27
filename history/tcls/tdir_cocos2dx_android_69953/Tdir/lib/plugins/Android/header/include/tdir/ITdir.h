//
//  ITdir.h
//  Tdir
//
//  Created by vvyang on 14-5-23.
//  Copyright (c) 2014年 vvyang. All rights reserved.
//

#ifndef Tdir_ITdir_h
#define Tdir_ITdir_h



#ifndef EXPORT_API

#if defined(_WIN32) || defined(_WIN64)
#define EXPORT_API __declspec(dllexport)
#else
#define EXPORT_API
#endif

#endif

#include <list>

#include "TdirNodeEntry.h"

namespace NApollo {

    class ITdir
    {
    public:
        enum Result
        {
            TDIR_NO_ERROR = 0,
            GET_OBJECT_FAILED = 1,
            CFGFILE_NOT_FOUND = 2,
            NEED_RECV_DONE_STATUS = 3,
            NEED_WAIT_FOR_RECV_STATUS = 4,
            NEED_INIT_BEFORE_QUERY = 5,

            //status
            WAIT_FOR_QUERY = 100,
            WAIT_FOR_RECV = 101,
            RECV_DONE = 102,
            UN_INIT = 103,

            // error reason
            TDIR_ERROR = 200,
            ALL_IP_CONNECT_FAILED = 201,
            ALLOCAT_MEMORY_FAILED = 202,
            SEND_TDIR_REQ_FAILED = 203,
            RECV_DIR_FAILED = 204,
            UNPACK_FAILED = 205,
            INIT_TGCPAPI_FAILED = 206,
            STOP_SESSION_FAILED = 207,
            TGCPAPI_ERROR = 208,
            PARAM_ERROR = 209,
            WAIT_SERVER_REP_TIMEOUT = 210
        };

        enum SvrFlag
        {
            NO_FLAG = 0,
            NEW = 1,
            RECOMMEND = 2,
            HOT = 3
        };

    public:
        virtual ITdir::Result Init(
            const int appId,
            const char* ipList,
            const char* portList,
            const char* lastSuccessIP = NULL,
            const char* lastSuccessPort = NULL,
            const char* openId = NULL
        ) = 0;

        virtual ITdir::Result Query(int timeout = 10) = 0;
        virtual ITdir::Result Recv(int timeout = 10) = 0;
        virtual ITdir::Result Status() = 0;
        virtual ITdir::Result SetSvrTimeout(int timeout = 5000) = 0;
        virtual ITdir::Result EnableLog() = 0;
        virtual ITdir::Result DisableLog() = 0;
        virtual ITdir::Result GetTreeNodes(std::list<TreeNode> &list) = 0;
    };
    
#ifdef __cplusplus
    extern "C"
    {
#endif
        EXPORT_API ITdir* TCLSCreateTdir();
        EXPORT_API void   TCLSRelease(ITdir** ppTdir);
        
#ifdef __cplusplus
    }
#endif

}
#endif
