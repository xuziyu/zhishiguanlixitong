package lu.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.kms.ISynchroNC.ISynchroNCInfoLocator;
import nc.itf.kms.ISynchroNC.ISynchroNCInfoPortType;
import nc.itf.kms.ISynchroNC.OuterSystemRetVO;
import nc.vo.kms.entityN.NCLabel;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.sys.utils.SynchroNCUtil;

public class Test1 {
	
	@Test
	public void test(){
		try {
			Gson gson = new Gson();
			URL url=new URL("http://localhost:634/uapws/service/ISynchroNCInfo");
			ISynchroNCInfoLocator is=new ISynchroNCInfoLocator();
			ISynchroNCInfoPortType http = is.getISynchroNCInfoSOAP11port_http(url);
			OuterSystemRetVO lable = http.getLable("design", "1999-10-10");
			//nc中的标签数据集合
			List<NCLabel> lables=new ArrayList<NCLabel>();
			lables = gson.fromJson(lable.getData(),
					new TypeToken<List<NCLabel>>() {
					}.getType());
			
			Map<String, String> relations = new HashMap<String, String>();
			//知识库标签的数据集合
			List<Label> knows=new ArrayList<Label>();
			Label label=null;
			for(NCLabel ncl:lables){
				label=new Label();
				label.setLabelvalue(ncl.getLableValue());
				//label.setParentId(ncl.getParentId());
				label.setId(ncl.getLabelId());
				label.setDelFlag(ncl.getDelFlag());
				knows.add(label);
				relations.put(label.getId(), ncl.getParentId());
			}
			System.out.println(knows.size());
			Map<String, String> newRelations = new HashMap<String, String>(); // 存储key:机构id
			// value:机构的所有parentIds","分割
			String parentIds = "";
			//设置上下级
			int i=0;
			for (Label temp : knows) {
				//if(i>100)break;
				//parentIds = SynchroNCUtil.getParentIds(temp.getParentId(),
					//	relations);
				//temp.setParentIds(parentIds);
				newRelations.put(temp.getId(), parentIds);
				//System.out.println(temp.getId()+":"+temp.getLabelvalue()+":"+temp.getParentId()+":"+temp.getParentIds());
				//i++;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
