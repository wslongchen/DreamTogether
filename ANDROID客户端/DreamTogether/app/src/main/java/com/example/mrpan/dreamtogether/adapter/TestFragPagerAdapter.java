package com.example.mrpan.dreamtogether.adapter;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.fragment.CardDreamFragment;
import com.example.mrpan.dreamtogether.utils.Config;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class TestFragPagerAdapter extends FragmentPagerAdapter {

	private List<Dream> dreams;

	public TestFragPagerAdapter(FragmentManager fm,List<Dream> dreams) {
		super(fm);
		this.dreams=dreams;
	}

	@Override
	public Fragment getItem(int arg0) {
		Bundle bundle = new Bundle();
		bundle.putInt(Config.KEY, Config.images[arg0%getCount()]);
		CardDreamFragment frag = (CardDreamFragment) MainActivity.fragmentHashMap.get(CardDreamFragment.TAG);
		if(frag!=null) {
			frag.setDream(dreams.get(arg0%getCount()));
		}
		//frag.setArguments(bundle);
		return frag;
	}

	@Override
	public int getCount() {
		return dreams.size();
	}
}
