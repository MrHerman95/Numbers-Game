package com.example.numbersgame.presentation

import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.numbersgame.R

private const val COLOR_RED = 0xFFFF4444
private const val COLOR_GREEN = 0xFF008000

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

@BindingAdapter("emojiResult")
fun bindEmojiResult(imageView: ImageView, winner: Boolean) {
    val emojiResult = if (winner) R.drawable.ic_smile else R.drawable.ic_sad
    imageView.setImageResource(emojiResult)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("requiredPercent")
fun bindRequiredPercent(textView: TextView, percent: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.required_percentage),
        percent
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.score_answers),
        count
    )
}

@BindingAdapter("questionsCount", "rightAnswersCount")
fun bindScorePercent(textView: TextView, questionsCount: Int, rightAnswersCount: Int) {
    textView.text = String.format(
        textView.context.resources.getString(R.string.score_percentage),
        getPercentOfRightAnswers(questionsCount, rightAnswersCount)
    )
}

private fun getPercentOfRightAnswers(questionsCount: Int, rightAnswersCount: Int): Int {
    return if (questionsCount == 0) {
        0
    } else {
        (rightAnswersCount / questionsCount.toDouble() * 100).toInt()
    }
}

@BindingAdapter("progressTextColor")
fun bindProgressTextColor(textView: TextView, enoughAnswers: Boolean) {
    when (enoughAnswers) {
        false -> textView.setTextColor(COLOR_RED.toInt())
        true -> textView.setTextColor(COLOR_GREEN.toInt())
    }
}

@BindingAdapter("progressBar")
fun bindProgressBar(progressBar: ProgressBar, percent: Int) {
    progressBar.setProgress(percent, true)
}

@BindingAdapter("progressBarColor")
fun bindProgressBarColor(progressBar: ProgressBar, enoughPercent: Boolean) {
    when (enoughPercent) {
        false -> progressBar.progressTintList = ColorStateList.valueOf(COLOR_RED.toInt())
        true -> progressBar.progressTintList = ColorStateList.valueOf(COLOR_GREEN.toInt())
    }
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}