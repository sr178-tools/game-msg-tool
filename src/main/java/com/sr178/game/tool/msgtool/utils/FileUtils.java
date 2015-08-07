package com.sr178.game.tool.msgtool.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.io.Files;
import com.sr178.game.tool.msgtool.bean.struct.doc.DirBean;
import com.sr178.game.tool.msgtool.fileparse.Type;

public class FileUtils {
	 public static final String LINE = "\r\n";
	 public static ThreadLocal<Type> hold = new ThreadLocal<Type>();
	 public  static void readfile(String fileRootPath,String fileName,List<File> list){
		   File file = new File(fileRootPath,fileName);
			if(!file.isDirectory()){
				list.add(file);
			}else{
				String[] filelist = file.list();
				fileRootPath = fileRootPath+"//"+fileName;
				for (int i = 0; i < filelist.length; i++) {
		            File readfile = new File(fileRootPath,filelist[i]);
		            if (!readfile.isDirectory()) {
		            	list.add(readfile);
		            } else if (readfile.isDirectory()) {
		            	if(hold.get()==Type.LUA&&readfile.getName().equals("server")){
		            		//客户端不需要生成server目录下的文件
		            		continue;
		            	}
		            	readfile(fileRootPath,readfile.getName(),list);
		            }
		    }
	      }
	   }
	 
	 public  static void readfileDirBean(String fileRootPath,String fileName,DirBean dirBean){
		   File file = new File(fileRootPath,fileName);
			if(!file.isDirectory()){
				dirBean.setFile(file);
			}else{
				dirBean.setFile(file);
				String[] filelist = file.list();
				fileRootPath = fileRootPath+"//"+fileName;
				for (int i = 0; i < filelist.length; i++) {
		            File readfile = new File(fileRootPath,filelist[i]);
		            DirBean dirBeanTemp =new DirBean();
		            dirBean.addChildDirBean(dirBeanTemp);
		            dirBeanTemp.setI(dirBean.getI()+1);
		            if (!readfile.isDirectory()) {
		            	dirBeanTemp.setFile(readfile);
		            } else if (readfile.isDirectory()) {
		            	System.out.println("是目录，继续递归 fileRootPath="+fileRootPath+",fileName="+readfile.getName());
		            	readfileDirBean(fileRootPath,readfile.getName(),dirBeanTemp);
		            }
		    }
	      }
	   }
	 
	 public static void writeFile(String str,String toPath){
		 File f = new File(toPath);
		 try {
			 Files.createParentDirs(f);
			Files.write(str.getBytes("utf-8"), f);
			System.out.println("成功生成文件"+toPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
}
