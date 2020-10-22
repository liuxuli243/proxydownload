package com.laoniu.controller;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.laoniu.entity.Music;
import com.laoniu.entity.MusicResponse;
import com.laoniu.utils.JiuKuUtils;

@Controller
@RequestMapping("/music")
public class MusicController {

	Logger logger = LoggerFactory.getLogger(MusicController.class);

	@RequestMapping("list")
	@ResponseBody
	public List<Music> list(HttpServletRequest request,String songname){
		logger.info(request.getRemoteAddr() + "搜索歌曲：" + songname);
		return JiuKuUtils.getSongList(songname);
	}
	
	@RequestMapping("download")
	public void download(int songid,HttpServletRequest request,HttpServletResponse response) {
		MusicResponse musicInfo = JiuKuUtils.getMusicInfoBySongid(songid);
		String mp3url = musicInfo.getWma();
		String songname = musicInfo.getMname();
		String singer = musicInfo.getSinger();
		String filename = singer + "-" + songname + ".mp3";
		logger.info(request.getRemoteAddr() + "下载歌曲：" + filename);
		OutputStream out = null;
		DataInputStream in = null;
		try {
			URL url = new URL(mp3url);
			HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
			urlCon.setConnectTimeout(6000);
			urlCon.setReadTimeout(6000);
			 int code = urlCon.getResponseCode();
			 if (code == HttpURLConnection.HTTP_OK) {
				in = new DataInputStream(urlCon.getInputStream());
				response.reset();
		        response.setContentType("application/octet-stream; charset=utf-8");
		        String nfileName=URLEncoder.encode(filename,"UTF-8");
		        String cd="attachment; filename="+nfileName+";filename*=UTF-8''"+nfileName;
//			        if ("FF".equals(getBrowser(request))) {  
//			            // 针对火狐浏览器处理方式不一样了  
////			        	nfileName = new String(fileName.getBytes("UTF-8"),"iso-8859-1");  
//			        }
		        response.setHeader("Content-Disposition", cd);
		        out = response.getOutputStream();
		        byte[] buffer = new byte[2048];
		        int count = 0;
		        while ((count = in.read(buffer)) > 0) {
		        	out.write(buffer, 0, count);
		        }
			 }else {
				 logger.info("连接失败");
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 试听
	*Title: audition
	*author:liuxuli
	*Description: 
	　 * @param songid
	　 * @return
	 */
	@RequestMapping("audition")
	public String audition(int songid,HttpServletRequest request) {
		MusicResponse musicInfo = JiuKuUtils.getMusicInfoBySongid(songid);
		request.setAttribute("music", musicInfo);
		logger.info(request.getRemoteAddr() + "试听歌曲：" + musicInfo.getSinger() + "-" + musicInfo.getMname());
		return "audition";
	}
	
}
