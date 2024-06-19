package com.example.finalapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.finalapplication.databinding.ActivityLoginBinding
import com.example.finalapplication.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //상태변화
        changeVisibility(intent.getStringExtra("status").toString())

        //회원가입
        binding.goSignInBtn.setOnClickListener {
            changeVisibility("signin")
        }

        binding.signBtn.setOnClickListener {    // 가입 Button
            val email = binding.authIdEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    binding.authIdEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener{sendTask ->
                                if(sendTask.isSuccessful){
                                    Toast.makeText(baseContext,"회원가입 완료", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "회원가입 성공!!")
                                    changeVisibility("logout")
                                }
                                else{
                                    Toast.makeText(baseContext,"메일발송 실패", Toast.LENGTH_SHORT).show()
                                    Log.d("mobileapp", "메일발송 실패")
                                    changeVisibility("logout")
                                }
                            }
                    }
                    else{
                        Toast.makeText(baseContext,"회원가입 실패", Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "== ${task.exception} ==")
                        changeVisibility("logout")
                    }
                }
        }

        //로그인
        binding.loginBtn.setOnClickListener {   // 로그인 Button
            val email = binding.authIdEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    binding.authIdEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if(task.isSuccessful){
                        if(MyApplication.checkAuth()){
                            MyApplication.email = email
                            Log.d("mobileapp", "로그인 성공")
                            finish()
                        }
                        else{
                            Toast.makeText(baseContext,"이메일 인증이 되지 않았습니다.",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "이메일 인증 안됨")
                        }
                    }
                    else{
                        Toast.makeText(baseContext,"로그인 실패",Toast.LENGTH_SHORT).show()
                        Log.d("mobileapp", "로그인 실패")
                    }
                }
        }

        //로그아웃
        binding.logoutBtn.setOnClickListener {  // 로그아웃 Button
            MyApplication.auth.signOut()
            MyApplication.email = null
            Log.d("mobileapp", "로그 아웃")
            finish()
        }

        //구글 인증
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            Log.d("mobileapp","account1 : ${task.toString()}")
            //Log.d("mobileapp","account2 : ${task.result}")
            try{
                val account = task.getResult(ApiException::class.java)
                val crendential = GoogleAuthProvider.getCredential(account.idToken, null)
                MyApplication.auth.signInWithCredential(crendential)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){ //로그인 되었을때
                            MyApplication.email = account.email
                            Toast.makeText(baseContext,"구글 로그인 성공",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 성공")
                            finish()
                        }
                        else{
                            changeVisibility("logout")
                            Toast.makeText(baseContext,"구글 로그인 실패",Toast.LENGTH_SHORT).show()
                            Log.d("mobileapp", "구글 로그인 실패")
                        }
                    }
            }catch (e: ApiException){ // APIException은 이미 지정된 exception말고 custom한 exception을 만들어서 쓰고 싶을때 사용
                changeVisibility("logout")
                Toast.makeText(baseContext,"구글 로그인 Exception : ${e.printStackTrace()},${e.statusCode}",Toast.LENGTH_SHORT).show()
                Log.d("mobileapp", "구글 로그인 Exception : ${e.message}, ${e.statusCode}")
            }
        }

        binding.googleLoginBtn.setOnClickListener { // 구글인증 Button
            val gso = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_client_id))
                .requestEmail()
                .build()
            val signInIntent = GoogleSignIn.getClient(this,gso).signInIntent
            requestLauncher.launch(signInIntent)
        }

    }

    //상태 변화 함수
    fun changeVisibility(mode:String){
        if(mode.equals("login")){       // 현재 로그인 상태
            binding.run{
                authoutTextView.text = "정말 로그아웃하시겠습니까?"
                authoutTextView.visibility = View.VISIBLE
                logoutBtn.visibility = View.VISIBLE
                authMainTextView.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility= View.GONE
                googleLoginBtn.visibility = View.GONE
            }
        }
        else if(mode.equals("logout")){ // 현재 로그아웃 상태
            binding.run{
                authIdEditView.visibility = View.VISIBLE //아이디입력
                authPasswordEditView.visibility = View.VISIBLE //비밀번호입력
                loginBtn.visibility= View.VISIBLE //로그인하기
                logoutBtn.visibility = View.GONE
                authMainTextView.text = "아직 계정이 없으신가요?"
                authMainTextView.visibility = View.VISIBLE
                goSignInBtn.visibility = View.VISIBLE //회원가입 버튼
                signBtn.visibility = View.GONE //가입버튼
                googleLoginBtn.visibility = View.VISIBLE //구글인증버튼
            }
        }
        else if(mode.equals("signin")){    // 회원가입 버튼 클릭 : 회원가입 진행 상태
            binding.run{
                authIdEditView.visibility = View.VISIBLE //아이디입력
                authPasswordEditView.visibility = View.VISIBLE //비밀번호입력
                signBtn.visibility = View.VISIBLE //가입버튼
                authMainTextView.visibility = View.GONE
                goSignInBtn.visibility = View.GONE //회원가입버튼
                logoutBtn.visibility = View.GONE
                loginBtn.visibility= View.GONE //로그인하기
                googleLoginBtn.visibility = View.GONE//구글인증버튼
            }
        }
    }
}