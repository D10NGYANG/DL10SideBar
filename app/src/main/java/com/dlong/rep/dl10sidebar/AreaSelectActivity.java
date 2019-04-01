package com.dlong.rep.dl10sidebar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dlong.rep.dl10sidebar.adapter.AreaCodeListAdapter;
import com.dlong.rep.dl10sidebar.bean.AreaPhoneBean;
import com.dlong.rep.dl10sidebar.utils.PinyinUtils;
import com.dlong.rep.dl10sidebar.utils.ReadAssetsJsonUtil;
import com.dlong.rep.dlsidebar.DLSideBar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

public class AreaSelectActivity extends AppCompatActivity implements View.OnClickListener{
    private Context mContext = this;
    private AreaCodeListAdapter mAdapter;
    private ArrayList<AreaPhoneBean> mBeans = new ArrayList<>();

    /** 记录当前是否是search图标 */
    private boolean IS_SEARCH_ICON = true;

    private ImageView imgSearch;
    private ImageView imgBack;
    private TextView txtTittle;
    private EditText etSearch;
    private ListView lvArea;
    private DLSideBar sbIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_area_select);
        // 防止输入法压缩布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        initView();
        initData();
    }

    private void initView() {
        imgSearch = findViewById(R.id.img_search);
        imgBack = findViewById(R.id.img_back);
        txtTittle = findViewById(R.id.txt_tittle);
        etSearch = findViewById(R.id.et_search);
        lvArea = findViewById(R.id.lv_area);
        sbIndex = findViewById(R.id.sb_index);

        imgSearch.setOnClickListener(this);
        imgBack.setOnClickListener(this);

        // 配置列表适配器
        lvArea.setVerticalScrollBarEnabled(false);
        lvArea.setFastScrollEnabled(false);
        mAdapter = new AreaCodeListAdapter(mContext, mBeans);
        lvArea.setAdapter(mAdapter);
        lvArea.setOnItemClickListener(mItemClickListener);
        // 配置右侧index
        sbIndex.setOnTouchingLetterChangedListener(mSBTouchListener);
        // 配置搜索
        etSearch.addTextChangedListener(mTextWatcher);
    }

    private void initData() {
        mBeans.clear();
        JSONArray array = ReadAssetsJsonUtil.getJSONArray(getResources().getString(R.string.area_select_json_name), mContext);
        if (null == array) array = new JSONArray();
        for (int i = 0; i < array.length(); i++) {
            AreaPhoneBean bean = new AreaPhoneBean();
            bean.name = array.optJSONObject(i).optString("zh");
            bean.fisrtSpell = PinyinUtils.getFirstSpell(bean.name.substring(0,1));
            bean.name_py = PinyinUtils.getPinYin(bean.name);
            bean.code = array.optJSONObject(i).optString("code");
            bean.locale = array.optJSONObject(i).optString("locale");
            bean.en_name = array.optJSONObject(i).optString("en");
            mBeans.add(bean);
        }
        // 根据拼音为数组进行排序
        Collections.sort(mBeans, new AreaPhoneBean.ComparatorPY());
        // 数据更新
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 右侧index选项监听
     */
    private DLSideBar.OnTouchingLetterChangedListener mSBTouchListener = new DLSideBar.OnTouchingLetterChangedListener() {
        @Override
        public void onTouchingLetterChanged(String str) {
            //触摸字母列时,将品牌列表更新到首字母出现的位置
            if (mBeans.size()>0){
                for (int i=0;i<mBeans.size();i++){
                    if (mBeans.get(i).fisrtSpell.compareToIgnoreCase(str) == 0) {
                        lvArea.setSelection(i);
                        break;
                    }
                }
            }
        }
    };

    /**
     * 搜索监听
     */
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!"".equals(s.toString().trim())) {
                //根据编辑框值过滤联系人并更新联系列表
                filterContacts(s.toString().trim());
                sbIndex.setVisibility(View.GONE);
            } else {
                sbIndex.setVisibility(View.VISIBLE);
                mAdapter.updateListView(mBeans);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 选项点击事件
     */
    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ArrayList<AreaPhoneBean> bs = mAdapter.getList();
            AreaPhoneBean bean =bs.get(position);
            Intent data = new Intent();
            data.putExtra("string", bean.name + "（+" + bean.code + "）");
            setResult(RESULT_OK, data);
            finish();
        }
    };

    /**
     * 比对筛选
     * @param filterStr
     */
    private void filterContacts(String filterStr){
        ArrayList<AreaPhoneBean> filters = new ArrayList<>();
        //遍历数组,筛选出包含关键字的item
        for (int i = 0; i < mBeans.size(); i++) {
            //过滤的条件
            if (isStrInString(mBeans.get(i).name_py,filterStr)
                    ||mBeans.get(i).name.contains(filterStr)
                    ||isStrInString(mBeans.get(i).fisrtSpell,filterStr)){
                //将筛选出来的item重新添加到数组中
                AreaPhoneBean filter = mBeans.get(i);
                filters.add(filter);
            }
        }

        //将列表更新为过滤的联系人
        mAdapter.updateListView(filters);
    }

    /**
     * 判断字符串是否包含
     * @param bigStr
     * @param smallStr
     * @return
     */
    public boolean isStrInString(String bigStr,String smallStr){
        return bigStr.toUpperCase().contains(smallStr.toUpperCase());
    }

    /**
     * 修改当前显示
     */
    private void changeMode(){
        if (IS_SEARCH_ICON){
            imgSearch.setImageResource(R.mipmap.search);
            imgSearch.setVisibility(View.VISIBLE);
            etSearch.setText("");
            etSearch.setVisibility(View.GONE);
            txtTittle.setVisibility(View.VISIBLE);
        } else {
            imgSearch.setImageResource(R.mipmap.clean);
            imgSearch.setVisibility(View.INVISIBLE);
            etSearch.setVisibility(View.VISIBLE);
            etSearch.setFocusable(true);
            txtTittle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                if (IS_SEARCH_ICON){
                    finish();
                } else {
                    IS_SEARCH_ICON = true;
                    changeMode();
                }
                break;
            case R.id.img_search:
                if (IS_SEARCH_ICON){
                    IS_SEARCH_ICON = false;
                    changeMode();
                } else {
                    etSearch.setText("");
                }
                break;
        }
    }
}
