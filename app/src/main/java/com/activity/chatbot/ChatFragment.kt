package com.activity.chatbot
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.activity.chatbot.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    val binding get() = _binding!!
    lateinit var viewModel: ChatViewModel
    private var messageAdapter = MessageAdapter()
    private var messageList = mutableListOf<Pair<String, Int>>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rc_chat)
        val geminiAi = GenerativeModel(
            modelName = "gemini-3-flash-preview",
            apiKey = "ISI API MU DISINI",
            systemInstruction = content { text("Kamu adalah ibu guru pemrograman web yang santai dan menyenangkan. Kamu boleh bercanda ringan sesekali, tapi tetap sopan dan profesional. Fokus pada HTML, CSS, dan JavaScript dasar. Jelaskan dengan poin-poin, beri contoh singkat, dan ajak siswa mencoba. Jangan bertele-tele.") }

        )

        viewModel = ViewModelProvider(
            this,
            ChatViewModel.ChatViewModelFactory(geminiAi)
        ).get(ChatViewModel::class.java)
        messageList.add(Pair("Saya adalah guru pemrograman Web kamu! Ada yang bisa saya bantu?", MessageAdapter.VIEW_TYPE_GEMINI))
        messageAdapter.setMessages(messageList)
        recyclerView.adapter = messageAdapter
        setAdapter()
        sendMessage()
        observe()

    }

    private fun setAdapter() {
        val ll = LinearLayoutManager(requireContext())
        ll.stackFromEnd = true
        binding.rcChat.layoutManager = ll
        binding.rcChat.setHasFixedSize(true)
        binding.rcChat.adapter = messageAdapter
    }

    private fun sendMessage() {
        binding.ivSend.setOnClickListener {
            val userMessage = binding.evSend.text.toString().trim()
            viewModel.geminiChat(userMessage)
            messageList.add(Pair(userMessage,MessageAdapter.VIEW_TYPE_USER))
            messageAdapter.setMessages(messageList)
            scrollPosition()
            binding.evSend.setText("")
            binding.imagePromptProgress.visibility = View.VISIBLE
            val inputMethod = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethod.hideSoftInputFromWindow(it.windowToken,0)
        }
    }
    private fun observe(){
        viewModel.messageResponse.observe(viewLifecycleOwner, Observer { content->
            content.text?.let {
                messageList.add(Pair(it,MessageAdapter.VIEW_TYPE_GEMINI))
                messageAdapter.setMessages(messageList)
                binding.imagePromptProgress.visibility = View.GONE
                scrollPosition()
            }
        })
    }
    private fun scrollPosition(){
        binding.rcChat.smoothScrollToPosition(messageAdapter.itemCount - 1)
    }
}