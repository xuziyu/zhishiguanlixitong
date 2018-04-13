/**
 * ISynchroNCInfoPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package nc.itf.kms.ISynchroNCInfo;

public interface ISynchroNCInfoPortType extends java.rmi.Remote {
    public nc.vo.kms.entityN.OuterSystemRetVO getNCGroupInfo(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
    public nc.vo.kms.entityN.OuterSystemRetVO getNCOrgInfo(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
    public nc.vo.kms.entityN.OuterSystemRetVO getNCDeptInfo(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
    public nc.vo.kms.entityN.OuterSystemRetVO getNCUserInfo(java.lang.String string, java.lang.String string1, java.lang.String string2, java.lang.String string3) throws java.rmi.RemoteException;
}
