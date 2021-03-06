package com.neppplus.colosseum_20211117.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.neppplus.colosseum_20211117.R
import com.neppplus.colosseum_20211117.ViewReplyDetailActivity
import com.neppplus.colosseum_20211117.ViewTopicDetailActivity
import com.neppplus.colosseum_20211117.datas.ReplyData
import com.neppplus.colosseum_20211117.datas.TopicData
import com.neppplus.colosseum_20211117.utils.ServerUtil
import org.json.JSONObject
import org.w3c.dom.Text
import java.text.SimpleDateFormat

class ReplyAdapter(
    val mContext: Context,
    val resId: Int,
    val mList: List<ReplyData>
) : ArrayAdapter<ReplyData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView

        if (tempRow == null) {

            tempRow = mInflater.inflate(R.layout.reply_list_item, null)

        }

        val row = tempRow!!

        val data = mList[position]

        val txtReplyContent = row.findViewById<TextView>(R.id.txtReplyContent)
        val txtWriterNickname = row.findViewById<TextView>(R.id.txtWriterNickname)
        val txtSelectedSide = row.findViewById<TextView>(R.id.txtSelectedSide)
        val txtCreatedAt = row.findViewById<TextView>(R.id.txtCreatedAt)
        val txtReplyCount = row.findViewById<TextView>(R.id.txtReplyCount)
        val txtLikeCount = row.findViewById<TextView>(R.id.txtLikeCount)
        val txtDislikeCount = row.findViewById<TextView>(R.id.txtDislikeCount)

        txtReplyContent.text = data.content

        txtWriterNickname.text = data.writer.nickname

        txtSelectedSide.text = "(${data.selectedSide.title})"

        txtCreatedAt.text = data.getFormattedCreatedAt()

        txtReplyCount.text = "?????? : ${data.replyCount}???"
        txtLikeCount.text = "????????? : ${data.likeCount}???"
        txtDislikeCount.text = "????????? : ${data.dislikeCount}???"

//        ??? ????????? ????????? ?????? ????????? ??? ?????? / ?????? ??? ??????
        if (data.myLike) {

//            red_border_box??? txtLikeCount??? ????????? ??????
            txtLikeCount.setBackgroundResource(R.drawable.red_border_box)

            txtLikeCount.setTextColor(mContext.resources.getColor(R.color.red))

        } else {

            txtLikeCount.setBackgroundResource(R.drawable.gray_border_box)

            txtLikeCount.setTextColor(mContext.resources.getColor(R.color.gray))

        }

//        ??? ?????? ????????? ????????? ????????? ??????
        txtLikeCount.setOnClickListener {

//            ??? ????????? ???????????? ???????????? -> ?????? API ??????
            ServerUtil.postRequestReplyLikeOrDislike(
                mContext,
                data.id,
                true,
                object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObject: JSONObject) {

//                ?????? ?????? ?????? ????????? ?????? ??????
//                => ?????? ?????? ?????? ?????? ???????????? (????????? ???????????? ???)

                        (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()

                    }

                })

        }


//        ??? ????????? ????????? ?????? ????????? ??? ?????? / ?????? ??? ??????
        if (data.myDislike) {

//            blue_border_box??? txtDislikeCount??? ????????? ??????
            txtDislikeCount.setBackgroundResource(R.drawable.blue_border_box)

            txtDislikeCount.setTextColor(mContext.resources.getColor(R.color.blue))

        } else {

            txtDislikeCount.setBackgroundResource(R.drawable.gray_border_box)

            txtDislikeCount.setTextColor(mContext.resources.getColor(R.color.gray))

        }

//        ??? ?????? ????????? ????????? ????????? ??????
        txtDislikeCount.setOnClickListener {

//            ??? ????????? ???????????? ???????????? -> ?????? API ??????

            ServerUtil.postRequestReplyLikeOrDislike(
                mContext,
                data.id,
                false,
                object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(jsonObject: JSONObject) {

                        (mContext as ViewTopicDetailActivity).getTopicDetailFromServer()

                    }

                })

        }

        txtReplyCount.setOnClickListener {

            val myIntent = Intent(mContext, ViewReplyDetailActivity::class.java)
            myIntent.putExtra("reply", data)
            mContext.startActivity(myIntent)

        }

        return row
    }
}




