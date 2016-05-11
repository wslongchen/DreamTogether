package com.example.mrpan.dreamtogether.adapter;

import com.example.mrpan.dreamtogether.MainActivity;
import com.example.mrpan.dreamtogether.OtherActivity;
import com.example.mrpan.dreamtogether.entity.Dream;
import com.example.mrpan.dreamtogether.fragment.CardDreamFragment;
import com.example.mrpan.dreamtogether.utils.Config;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class TestFragPagerAdapter extends FragmentStatePagerAdapter {

	private List<Dream> dreams;

	public TestFragPagerAdapter(FragmentManager fm,List<Dream> dreams) {
		super(fm);
		this.dreams=dreams;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		CardDreamFragment f = (CardDreamFragment) super.instantiateItem(container, position);
		f.setDream(dreams.get(position % getCount()));
		return f;
	}

	@Override
	public Fragment getItem(int arg0) {
		//Bundle bundle = new Bundle();
		//bundle.putInt(Config.KEY, Config.images[arg0 % getCount()]);
		Fragment frag =null;
//		frag=OtherActivity.fragmentHashMap.get(CardDreamFragment.TAG);
//		if(frag!=null) {
//			((CardDreamFragment)frag).setDream(dreams.get(arg0%getCount()));
//		}
		frag=new CardDreamFragment();

		//frag.setArguments(bundle);
		return frag;
	}

	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
	}

	@Override
	public int getCount() {
		return dreams.size();
	}
}
