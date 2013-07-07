package geogebra.touch;

import geogebra.common.main.App;
import geogebra.common.main.Localization;
import geogebra.common.move.ggtapi.models.Material;
import geogebra.common.move.ggtapi.models.Material.MaterialType;
import geogebra.html5.util.ggtapi.JSONparserGGT;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.ListBox;

public class FileManagerM {
	private static final String FILE_PREFIX = "file#";
	private static final String THUMB_PREFIX = "img#";
	private static final String META_PREFIX = "meta#";
	protected Storage stockStore = Storage.getLocalStorageIfSupported();
	public FileManagerM(){
		if(this.stockStore != null){
			ensureKeyPrefixes();
		}
	}
	
	private void ensureKeyPrefixes() {
		if (this.stockStore.getLength() > 0)
		{
			for (int i = 0; i < this.stockStore.getLength(); i++)
			{
				String oldKey = this.stockStore.key(i);
				if(!oldKey.contains("#")){
					this.stockStore.removeItem(oldKey);
				}else{
					App.debug(stockStore.getItem(oldKey));
				}
			}
		}	
	}
	
	public void toList(ListBox fileList){
		fileList.clear();

		if (this.stockStore == null)
		{
			return;
		}

		if (this.stockStore.getLength() > 0)
		{
			for (int i = 0; i < this.stockStore.getLength(); i++)
			{
				String key = this.stockStore.key(i);
				if(key.startsWith(FILE_PREFIX)){
					fileList.addItem(key.substring(FILE_PREFIX.length()));
				}
			}
		}

	}

	public void delete(String text) {
		this.stockStore.removeItem(FILE_PREFIX+text);
		this.stockStore.removeItem(THUMB_PREFIX+text);
		TouchEntryPoint.browseGUI.loadAllFiles();
	}

	public void saveFile(String consTitle, App app) {
		this.stockStore.setItem(FILE_PREFIX+consTitle, app.getXML());
		
		//extract metadata
		Material mat = new Material(0,MaterialType.ggb);
		mat.setTimestamp(System.currentTimeMillis() / 1000);
		mat.setTitle(consTitle);
		mat.setDescription(app.getKernel().getConstruction().getWorksheetText(0));
		
		this.stockStore.setItem(META_PREFIX+consTitle,mat.toJson().toString());
		TouchEntryPoint.browseGUI.loadAllFiles();
	}

	public String getFile(String text) {
		return this.stockStore.getItem(FILE_PREFIX+text);
	}

	public List<Material> search(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Material> getAllFiles() {
		List<Material> ret = new ArrayList<Material>();
		if (this.stockStore == null || this.stockStore.getLength() <=0)
		{
			return ret;
		}

		for (int i = 0; i < this.stockStore.getLength(); i++)
		{
			String key = this.stockStore.key(i);
			if(key.startsWith(FILE_PREFIX)){
				String keyStem = key.substring(FILE_PREFIX.length());
				Material mat = JSONparserGGT.parseMaterial(this.stockStore.getItem(META_PREFIX+keyStem));
				if(mat == null){
					mat = new Material(0,MaterialType.ggb);
					mat.setTitle(keyStem);
				}
				mat.setURL(keyStem);
				ret.add(mat);
			}
		}

		return ret;
	}
	
	public String getDefaultConstructionTitle(Localization loc) {		
		int i = 1;
		String filename;
		do{
			filename = loc.getPlain("UntitledA",i+"");
			i++;
		}
		while (this.stockStore != null && this.stockStore.getItem(FILE_PREFIX + filename)!=null);
		return filename;
	}
}
