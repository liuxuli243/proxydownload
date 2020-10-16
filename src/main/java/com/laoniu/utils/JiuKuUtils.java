package com.laoniu.utils;

import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.laoniu.entity.Music;
import com.laoniu.entity.MusicResponse;

public class JiuKuUtils {

	/**
	 * 获取歌曲列表
	*Title: getSongList
	*author:liuxuli
	*Description: 
	　 * @param songname
	　 * @return
	 */
	@SuppressWarnings("deprecation")
	public static List<Music> getSongList(String songname) {
		List<Music> musiclist = Lists.newArrayList();
		String searchurl = "http://baidu.9ku.com/suggestions/?kw=" + URLEncoder.encode(songname);
		//System.out.println("搜索歌曲："+songname);
		String responseresult = HttpClientUtils.doGet(searchurl);
		//System.out.println(responseresult);
		if(responseresult.length() <= 0) {
			return musiclist;
		}
		Document document = Jsoup.parse(responseresult);
		Elements elementsByClass = document.getElementsByClass("sug-song");
		Elements elementsByTag = elementsByClass.get(0).getElementsByTag("dd");
		for (Element element : elementsByTag) {
			Elements spans = element.getElementsByTag("span");
			Elements songa = element.getElementsByTag("a");
			String href = songa.get(0).attr("href");
			//通过截取获取歌曲的ID
			String songid = href.substring(24, href.length()-4);
			//歌名
			String songnames = spans.get(0).text();
			//歌手名
			String singgername = spans.get(1).text();
			//System.out.println(songid + ":" + songnames + ":" + singgername.substring(1));
			Music music = new Music();
			music.setSongid(Integer.valueOf(songid));
			music.setSongname(songnames);
			music.setSingername(singgername.substring(1));
			musiclist.add(music);
		}
		return musiclist;
	}
	/**
	 * 获取音乐的相关信息
	*Title: getMusicInfoBySongid
	*author:liuxuli
	*Description: 
	　 * @param songid
	　 * @return
	 */
	public static MusicResponse getMusicInfoBySongid(int songid) {
		String getmp3url = "http://www.9ku.com/html/playjs/" + (songid / 1000 + 1) + "/" + songid + ".js";
		//System.out.println(getmp3url);
		String result = HttpClientUtils.doGet(getmp3url);
		result = result.substring(1,result.length()-1);
		MusicResponse  musicobj = new MusicResponse();
		if (result.length() > 0) {
			musicobj = JSONObject.parseObject(result, MusicResponse.class);
		}
		return musicobj;
	}
	public static void main(String[] args) {
		MusicResponse musicInfoBySongid = getMusicInfoBySongid(43955);
		System.out.println(JSONObject.toJSONString(musicInfoBySongid));
	}
}
