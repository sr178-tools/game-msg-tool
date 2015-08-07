package com.sr178.game.tool.msgtool.bean.struct.doc;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

public class DirBean {
   private File file;
   private int i=1;
   private List<DirBean> list = Lists.newArrayList();
   public File getFile() {
	   return file;
    }
	public void setFile(File file) {
		this.file = file;
	}
	public void setList(List<DirBean> list) {
		this.list = list;
	}
	public void addChildDirBean(DirBean dirBean){
		   list.add(dirBean);
	}
	public DirBean(File file) {
		this.file = file;
	}
	public boolean isDirectory(){
		return file.isDirectory();
	}
	public int getChildSize(){
		return list.size();
	}
	public DirBean() {
	}
	public int getI() {
		return i;
	}
	public void setI(int i) {
		this.i = i;
	}
	public List<DirBean> getList() {
		return list;
	}
	
	
}
