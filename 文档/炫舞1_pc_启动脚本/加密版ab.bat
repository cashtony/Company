��
echo off
net use \\h3d-131\ab_proj$$ /del
net use \\h3d-131\IPC$ /del
net use \\h3d-ab01\ab_proj$$ /del
net use \\h3d-ab01\IPC$ /del
net use \\h3d-ab02\ab_proj$$ /del
net use \\h3d-ab02\IPC$ /del

@echo ɾ���������
net use  \\h3d-131   /user:administrator h3d!#!
echo ��¼h3d-131�ɹ�

net use  \\h3d-ab01   /user:abuser abuser
echo ��¼h3d-ab01�ɹ�

net use  \\h3d-ab02   /user:abuser abuser

echo ��¼h3d-ab02�ɹ�

pause
