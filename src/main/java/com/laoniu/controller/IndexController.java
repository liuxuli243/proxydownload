package com.laoniu.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.laoniu.entity.Music;
import com.laoniu.utils.JiuKuUtils;

@Controller
public class IndexController {

	@RequestMapping("/")
	public String list(HttpServletRequest request,String songname) {
		List<Music> songList = null;
		if (songname != null && songname.length() > 0) {
			System.out.println(request.getRemoteAddr() + " 搜索：" + songname);;
			songList = JiuKuUtils.getSongList(songname);
		}else {
			songList = Lists.newArrayList();
		}
		request.setAttribute("songname", songname);
		request.setAttribute("songList", songList);
		return "musiclist";
	}
}
