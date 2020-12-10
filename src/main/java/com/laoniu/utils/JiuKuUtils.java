package com.laoniu.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.laoniu.entity.Music;
import com.laoniu.entity.MusicLrc;
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
	
	@SuppressWarnings("deprecation")
	public static List<Music> getSearchSongList(String songname) {
		List<Music> musiclist = Lists.newArrayList();
		String searchurl = "http://baidu.9ku.com/song/?key=" + URLEncoder.encode(songname);
		//System.out.println("搜索歌曲："+songname);
		String responseresult = HttpClientUtils.doGet(searchurl);
		//System.out.println(responseresult);
		if(responseresult.length() <= 0) {
			return musiclist;
		}
		Document document = Jsoup.parse(responseresult);
		Elements elementsByClass = document.getElementsByClass("songList");
		Elements elementsByTag = elementsByClass.get(0).getElementsByTag("li");
		for (Element element : elementsByTag) {
			//Elements spans = element.getElementsByTag("span");
			Elements songa = element.getElementsByTag("a");
			String href = songa.get(0).attr("href");
			//通过截取获取歌曲的ID
			String songid = href.substring(24, href.length()-4);
			//歌名
			String songnames = songa.get(0).text();
			//歌手名
			String singgername = songa.get(1).text();
			//System.out.println(songid + ":" + songnames + ":" + singgername.substring(1));
			Music music = new Music();
			music.setSongid(Integer.valueOf(songid));
			music.setSongname(songnames);
			music.setSingername(singgername);
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
	
	public static List<MusicLrc> getMusiclrc(int songid) {
		String url = "http://www.9ku.com/play/"+ songid +".htm";
		String result = HttpClientUtils.doGet(url);
		Document document = Jsoup.parse(result);
		String text = document.getElementById("lrc_content").text();
		String[] split = text.split("\n");
		List<MusicLrc> lrcs = new ArrayList<MusicLrc>();
		for (String lrcline : split) {
			if (lrcline.indexOf("[") != -1) {
				int lastIndexOf = lrcline.lastIndexOf("]");
				String lrctime = lrcline.substring(0, lastIndexOf+1);
				String lrc = lrcline.substring(lastIndexOf+1);
				String[] times = lrctime.split("]");
				for (String time : times) {
					time = time.substring(1);
					if (istime(time)) {
						int miunite = Integer.valueOf(time.substring(0, 2));
						int second = Integer.valueOf(time.substring(3, 5));
						int mis = Integer.valueOf(time.substring(6));
						Double minute = Double.valueOf(miunite);
						Double miao = Double.valueOf(second);
						Double ms = Double.valueOf(mis);
						String lrctimestr = String.format("%.4f", ((minute * 60) + miao) + (ms / 1000));
						MusicLrc musiclrc = new MusicLrc(lrctimestr, lrc);
						lrcs.add(musiclrc);
					}
				}
			}
		}
		lrcs.sort(new Comparator<MusicLrc>() {

			@Override
			public int compare(MusicLrc o1, MusicLrc o2) {
				double time1 = Double.valueOf(o1.getTime());
				double time2 = Double.valueOf(o2.getTime());
				return new Double((time1 - time2) * 1000).intValue();
			}
		});
		return lrcs;
	}
	
	public static boolean istime(String timestr) {
		if (timestr.indexOf(":") > -1 && timestr.indexOf(".") > 0) {
			String miunite = timestr.substring(0, 2);
			String second = timestr.substring(3, 5);
			String mis = timestr.substring(6);
			if(Pattern.matches("\\d+", miunite) && Pattern.matches("\\d+", second) && Pattern.matches("\\d+", mis)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	public static void main(String[] args) {
		/*MusicResponse musicInfoBySongid = getMusicInfoBySongid(43955);
		System.out.println(JSONObject.toJSONString(musicInfoBySongid));*/
		//getSearchSongList("少年");
		getMusiclrc(174927);
	}
}
