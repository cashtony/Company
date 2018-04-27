//
//  TdirNodeEntry.h
//  Tdir
//
//  Created by vvyang on 14-5-23.
//  Copyright (c) 2014年 vvyang. All rights reserved.
//

#ifndef Tdir_TdirNodeEntry_h
#define Tdir_TdirNodeEntry_h

#include <string>

#ifndef TDIR_COPY_CHAR_ARRAY
#define TDIR_COPY_CHAR_ARRAY(src, dest)  \
do {\
	if (NULL != dest) \
	{ \
		int len = strlen(dest) + 1; \
		src = new char[len]; \
		strncpy(src, dest, len); \
	} else {\
		src = NULL; \
	}\
} while (0)
#endif

#ifndef TDIR_COPY_UCHAR_ARRAY
#define TDIR_COPY_UCHAR_ARRAY(src, dest, len)  \
do {\
    if (NULL != dest) \
    { \
        src = new unsigned char[len]; \
        memcpy(src, dest, len); \
    } else {\
        src = NULL; \
    }\
} while (0)
#endif

#ifndef TDIR_SAFE_RELEASE_CHAR_ARRAY
#define TDIR_SAFE_RELEASE_CHAR_ARRAY(x) \
do { \
	if (x != NULL) \
	{ \
		delete[] x; \
		x = NULL; \
	}\
} while (0)
#endif

struct StaticInfo{
    int cltAttr;
    int cltAttr1;
    char* appAttr;
    char* curVersion;
    int windowAttr;
    int appID;
    int cltFlag;
    unsigned int bitmapMask;
    char* virConnUrl;
    StaticInfo()
    {
		appAttr = NULL;
        curVersion = NULL;
        virConnUrl = NULL;
    }
	StaticInfo(const StaticInfo &copy)
	{
		appAttr = NULL;
		curVersion = NULL;
		virConnUrl = NULL;

		TDIR_COPY_CHAR_ARRAY(appAttr, copy.appAttr);
		TDIR_COPY_CHAR_ARRAY(curVersion, copy.curVersion);
		TDIR_COPY_CHAR_ARRAY(virConnUrl, copy.virConnUrl);

		cltAttr = copy.cltAttr;
		cltAttr1 = copy.cltAttr1;
		windowAttr = copy.windowAttr;
		appID = copy.appID;
		cltFlag = copy.cltFlag;
		bitmapMask = copy.bitmapMask;
		
	}
	void operator=(const StaticInfo &copy)
	{
		appAttr = NULL;
		curVersion = NULL;
		virConnUrl = NULL;

		TDIR_COPY_CHAR_ARRAY(appAttr, copy.appAttr);
		TDIR_COPY_CHAR_ARRAY(curVersion, copy.curVersion);
		TDIR_COPY_CHAR_ARRAY(virConnUrl, copy.virConnUrl);

		cltAttr = copy.cltAttr;
		cltAttr1 = copy.cltAttr1;
		windowAttr = copy.windowAttr;
		appID = copy.appID;
		cltFlag = copy.cltFlag;
		bitmapMask = copy.bitmapMask;
	}
    ~StaticInfo()
    {
		TDIR_SAFE_RELEASE_CHAR_ARRAY(appAttr);
		TDIR_SAFE_RELEASE_CHAR_ARRAY(curVersion);
		TDIR_SAFE_RELEASE_CHAR_ARRAY(virConnUrl);
    }
};

struct DynamicInfo{
    char* appAttr;
    char* connectUrl;
    char* pingUrl;
    DynamicInfo()
    {
        appAttr = NULL;
        connectUrl = NULL;
        pingUrl = NULL;
    }

	DynamicInfo(const DynamicInfo &copy)
	{
		appAttr = NULL;
		connectUrl = NULL;
		pingUrl = NULL;
		
		TDIR_COPY_CHAR_ARRAY(appAttr, copy.appAttr);
		TDIR_COPY_CHAR_ARRAY(connectUrl, copy.connectUrl);
		TDIR_COPY_CHAR_ARRAY(pingUrl, copy.pingUrl);
	}

	void operator=(const DynamicInfo &copy)
	{
		appAttr = NULL;
		connectUrl = NULL;
		pingUrl = NULL;

		TDIR_COPY_CHAR_ARRAY(appAttr, copy.appAttr);
		TDIR_COPY_CHAR_ARRAY(connectUrl, copy.connectUrl);
		TDIR_COPY_CHAR_ARRAY(pingUrl, copy.pingUrl);
	}

    ~DynamicInfo()
    {
		TDIR_SAFE_RELEASE_CHAR_ARRAY(appAttr);
		TDIR_SAFE_RELEASE_CHAR_ARRAY(connectUrl);
		TDIR_SAFE_RELEASE_CHAR_ARRAY(pingUrl);
    }

};

struct UserRoleInfo
{
    unsigned long roleID;
    unsigned int lastLoginTime;
    char* roleName;
    char* roleLevel;
    unsigned int appLen;
    unsigned char* appBuff;
    
    UserRoleInfo()
    {
        roleName = NULL;
        roleLevel = NULL;
        appBuff = NULL;
    }
    
    UserRoleInfo(const UserRoleInfo& copy)
    {
        roleName = NULL;
        roleLevel = NULL;
        appBuff = NULL;
        
        roleID = copy.roleID;
        lastLoginTime = copy.lastLoginTime;
        appLen = copy.appLen;
        
        TDIR_COPY_CHAR_ARRAY(roleName, copy.roleName);
        TDIR_COPY_CHAR_ARRAY(roleLevel, copy.roleLevel);
        TDIR_COPY_UCHAR_ARRAY(appBuff, copy.appBuff, appLen);
    }
    
    void operator=(const UserRoleInfo& copy)
    {
        roleName = NULL;
        roleLevel = NULL;
        appBuff = NULL;
        
        roleID = copy.roleID;
        lastLoginTime = copy.lastLoginTime;
        appLen = copy.appLen;
        
        TDIR_COPY_CHAR_ARRAY(roleName, copy.roleName);
        TDIR_COPY_CHAR_ARRAY(roleLevel, copy.roleLevel);
        TDIR_COPY_UCHAR_ARRAY(appBuff, copy.appBuff, appLen);
    }
    
    ~UserRoleInfo()
    {
        TDIR_SAFE_RELEASE_CHAR_ARRAY(roleName);
        TDIR_SAFE_RELEASE_CHAR_ARRAY(roleLevel);
        TDIR_SAFE_RELEASE_CHAR_ARRAY(appBuff);
    }
};

struct TreeNode{
    int nodeID;
    int parentID;
    int flag;
    char* name;
    int status;
    int nodeType;
    int svrFlag;
    StaticInfo staticInfo;
    DynamicInfo dynamicInfo;
    std::vector<UserRoleInfo> userRoleInfo;
    TreeNode()
    {
        name = NULL;
    }

	TreeNode(const TreeNode &copy)
	{
		
		TDIR_COPY_CHAR_ARRAY(name, copy.name);
		staticInfo = copy.staticInfo;
		dynamicInfo = copy.dynamicInfo;
        userRoleInfo = copy.userRoleInfo;
		nodeID = copy.nodeID;
		parentID = copy.parentID;
		flag = copy.flag;
		status = copy.status;
		nodeType = copy.nodeType;
		svrFlag = copy.svrFlag;
	}

	void operator=(const TreeNode &copy)
	{
		TDIR_COPY_CHAR_ARRAY(name, copy.name);
		staticInfo = copy.staticInfo;
		dynamicInfo = copy.dynamicInfo;
        userRoleInfo = copy.userRoleInfo;
		nodeID = copy.nodeID;
		parentID = copy.parentID;
		flag = copy.flag;
		status = copy.status;
		nodeType = copy.nodeType;
		svrFlag = copy.svrFlag;
	}

    ~TreeNode()
    {
		TDIR_SAFE_RELEASE_CHAR_ARRAY(name);
    }
};


struct TreeCommonData{
    int ispCode;
    int provinceCode;
};
#endif
