package apollo.edus.collageweibo.ui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import apollo.edus.collageweibo.R;


public class EsSearchContactEditText extends LinearLayout implements OnClickListener, TextWatcher {
	private final static String TAG = EsSearchContactEditText.class.getName();
	private EditText seachInput;
	private View searchClear;
	private View searchIcon;
	private View tvSearchCancel;
	private View tvSearch;

	public EsSearchContactEditText(Context context) {
		super(context);
		init();
	}

	public EsSearchContactEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public EsSearchContactEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		View searchView = View.inflate(getContext(), R.layout.search_contact_input, null);
		addView(searchView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		seachInput = (EditText) searchView.findViewById(R.id.search_input);
		seachInput.setOnEditorActionListener(onEditorActionListener);
		searchIcon = searchView.findViewById(R.id.search_icon);
		searchClear = searchView.findViewById(R.id.search_clear);
		searchClear.setOnClickListener(this);
		seachInput.addTextChangedListener(this);
		rlOperation = searchView.findViewById(R.id.rl_operation);
		tvSearchCancel = searchView.findViewById(R.id.cancel);
		tvSearchCancel.setOnClickListener(this);
		tvSearch = findViewById(R.id.tv_search);
		tvSearch.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_clear:
			seachInput.setText("");
			if(listener != null){
				listener.onClearClicked();
			}
			break;
		case R.id.cancel:
			if(listener != null){
				listener.onCancelClicked();
			}
			break;
		case R.id.tv_search:
			if(listener != null){
				listener.onSearchClicked();
			}
			break;
		}
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if(listener != null){
			listener.beforeTextChanged(s, start, count, after);
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(listener != null){
			listener.onTextChanged(s, start, before, count);
		}
	}
	
	public void editTextRequestFocus(){
		seachInput.requestFocus();
	}
	
	public EditText getRealEditText(){
		return seachInput;
	}
	
	private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEARCH){
				onClick(tvSearch);
				return true;
			}
			return false;
		}
	};
	

	@Override
	public void afterTextChanged(Editable s) {
		if(s != null){
			if(!TextUtils.isEmpty(s.toString())){
				tvSearchCancel.setVisibility(View.GONE);
				tvSearch.setVisibility(View.VISIBLE);
				searchClear.setVisibility(View.VISIBLE);
			} else {
				tvSearchCancel.setVisibility(View.VISIBLE);
				tvSearch.setVisibility(View.GONE);
				searchClear.setVisibility(View.GONE);
			}
		} else {
			tvSearchCancel.setVisibility(View.VISIBLE);
			tvSearch.setVisibility(View.GONE);
			searchClear.setVisibility(View.GONE);
		}
		if(listener != null){
			listener.afterTextChanged(s);
		}
	}
	
	
	public String getText(){
		return seachInput.getText().toString();
	}
	
	public void setSeachEnable(boolean enabled){
		searchIcon.setEnabled(enabled);
		seachInput.setEnabled(enabled);
		tvSearch.setEnabled(enabled);
		searchIcon.setClickable(false);
		seachInput.setClickable(false);
		tvSearch.setClickable(false);
	}
	
	public boolean isEnable() {
		return searchIcon.isEnabled() && seachInput.isEnabled() && tvSearch.isEnabled();
	}

	
	public void setSearchContactListener(EsSearchContactListener listener){
		this.listener = listener;
	}
	
	public EsSearchContactListener listener;
	private View rlOperation;
	
	public interface EsSearchContactListener extends TextWatcher{
		void onCancelClicked();
		void onSearchClicked();
		void onClearClicked();
	}
	
	public void setOperationVisiability(int visiability){
		rlOperation.setVisibility(visiability);
	}
	
}
