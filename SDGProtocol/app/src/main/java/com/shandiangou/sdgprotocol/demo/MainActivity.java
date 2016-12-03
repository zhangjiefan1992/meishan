package com.shandiangou.sdgprotocol.demo;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.shandiangou.sdgprotocol.lib.ConnectionClient;
import com.shandiangou.sdgprotocol.lib.RequestCallBack;
import com.shandiangou.sdgprotocol.lib.protocol.BasicProtocol;
import com.shandiangou.sdgprotocol.lib.protocol.DataAckProtocol;
import com.shandiangou.sdgprotocol.lib.protocol.DataProtocol;
import com.shandiangou.sdgprotocol.lib.protocol.PingAckProtocol;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RequestCallBack {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ChatContent> chatContentList = new ArrayList<>();

    private Button btn_connect;
    private Button btn_close;
    private ImageButton ib_send;
    private EditText et_messageData;

    private ConnectionClient tcpLongConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiView();
    }

    private void intiView() {
        btn_connect = (Button) findViewById(R.id.connect_btn);
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpLongConnect = new ConnectionClient(MainActivity.this);
            }
        });

        btn_close = (Button) findViewById(R.id.close_btn);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tcpLongConnect.closeConnect();
            }
        });

        setConnStatus(false);

        ib_send = (ImageButton) findViewById(R.id.ib_send);
        ib_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = et_messageData.getText().toString().trim();
                chatAdapter.addMessage(new ChatContent(ChatContent.MYSELF, msg)); //往对话列表中添加一行

                if (tcpLongConnect == null) {
                    return;
                }

                int msgId = (int) (Math.random() * 2147483647);
                Log.e("linwb", "msgId: " + msgId);
                DataProtocol protocol = new DataProtocol();
                protocol.setPattion(0);   //业务类型为push
                protocol.setDtype(0);     //业务数据格式为json
                protocol.setMsgId(msgId);
                protocol.setData(msg);
                tcpLongConnect.addNewRequest(protocol); //往对话列表中添加一行

                recyclerView.scrollToPosition(chatAdapter.getAdapterSize() - 1);
                et_messageData.setText("");
            }
        });

        et_messageData = (EditText) findViewById(R.id.et_messageData);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.rv_recycleviewAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        chatAdapter = new ChatAdapter(this, chatContentList);
        recyclerView.setAdapter(chatAdapter);

        setTitle("未连接");
    }

    private void setConnStatus(boolean connected) {
        btn_connect.setClickable(!connected);
        btn_close.setClickable(connected);
        if (connected) {
            btn_connect.setBackgroundColor(Color.LTGRAY);
            btn_close.setBackgroundColor(Color.YELLOW);
        } else {
            btn_connect.setBackgroundColor(Color.YELLOW);
            btn_close.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public void onSuccess(BasicProtocol protocol) {
        if (protocol.getProtocolType() == 1) {
            String response = ((DataAckProtocol) protocol).getUnused();
            chatAdapter.addMessage(new ChatContent(ChatContent.GUEST, response));
            Log.e("linwb", "msgAck: " + response);
        } else if (protocol.getProtocolType() == 3) {
            String response = ((PingAckProtocol) protocol).getUnused();
            if (!TextUtils.equals("连接成功", getTitle())) {
                setTitle("连接成功");
                setConnStatus(true);
            }
            Log.e("linwb", "pingAck: " + response);
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        }
        Log.e("linwb", "-------------------------------------------");
    }

    @Override
    public void onFailed(int errorCode, String msg) {
        setTitle(msg);
        setConnStatus(false);
    }
}
