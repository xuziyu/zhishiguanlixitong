/**
 * ISynchroNCInfoPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package nc.itf.kms.ISynchroNC;


public interface ISynchroNCInfoPortType extends java.rmi.Remote {
    public OuterSystemRetVO getNCGroupInfo(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
    public OuterSystemRetVO getNCOrgInfo(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
    public OuterSystemRetVO getNCDeptInfo(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
    public OuterSystemRetVO getNCUserInfo(java.lang.String string, java.lang.String string1, java.lang.String string2, java.lang.String string3) throws java.rmi.RemoteException;
    public OuterSystemRetVO getLable(java.lang.String string, java.lang.String string1) throws java.rmi.RemoteException;
}
