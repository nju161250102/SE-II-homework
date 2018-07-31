package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.UserPO;

public interface UserDataService extends Remote {

	/**
	 * �����û�ʱ����ȡ�û�Ӧ�����е�Ψһid
	 * @return ��һ���û�Ӧ�����е�id [id��ʽ����λ�����ַ�������0001]
	 * @throws RemoteException
	 */
	public String getNewId() throws RemoteException;
	/**
	 * �����û���ID����һ��UserPO���󣬲�������û��Ƿ�ɾ��<br/>
	 * �Ҳ����ͷ���һ��null...
	 * @param id �û���id [id��ʽ����λ�����ַ�������0001]
	 * @return ���ҵ���UserPO����
	 * @throws RemoteException
	 */
	public UserPO findById(String id) throws RemoteException;
	/**
	 * �����ݿ�������һ��UserPO����
	 * @param user ����õ�UserPO����
	 * @return �Ƿ����ӳɹ�(���ݿ��д�г�������false)(id�Ѿ����ڷ���false)
	 * @throws RemoteException
	 */
	public boolean add(UserPO user) throws RemoteException;
	/**
	 * �����ݿ���ɾ��һ��UserPO����
	 * @param id ��Ҫɾ�����û���Ψһid [id��ʽ����λ�����ַ�������0001]
	 * @return �Ƿ�ɾ���ɹ�(���ݿ�����г�������false)(id�����ڷ���false)
	 * @throws RemoteException
	 */
	public boolean delete(String id) throws RemoteException;
	/**
	 * �������ݿ���һ���Ѿ����ڵ�UserPO����
	 * @param user ��Ҫ���µ�UserPO����
	 * @return �Ƿ���³ɹ�(���ݿ�����г�������false)(id�����ڷ���false)
	 * @throws RemoteException
	 */
	public boolean update(UserPO user) throws RemoteException;
	/**
	 * ��ȡ���ݿ�������δ��ɾ����UserPO�����б�
	 * @return ���ݿ�������δ��ɾ����UserPO����
	 * @throws RemoteException
	 */
	public ArrayList<UserPO> getAllUser() throws RemoteException;
	/**
	 * ��ѯ����������UserPO��¼
	 * @param field ��ѯ���ֶ�������Ҫ�����ݿⱣ��һ�£�
	 * @param content ��ѯ�����ݣ��Ƿ��ַ��ɿͻ��˹��ˣ�
	 * @param isfuzzy �Ƿ��ʴ�ģ�����ң�true��ʾ�ʴ�
	 * @return
	 * @throws RemoteException
	 */
	public ArrayList<UserPO> getUsersBy(String field, String content, boolean isfuzzy) throws RemoteException;
}