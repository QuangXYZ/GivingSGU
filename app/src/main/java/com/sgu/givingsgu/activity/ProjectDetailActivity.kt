package com.sgu.givingsgu.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.databinding.ActivityProjectDetailBinding


class ProjectDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProjectDetailBinding
    var isExpanded: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()

        }


    fun init() {
        val fullText =
            "[CHIẾN DỊCH XÂY NHÀ VỆ SINH TẠI MÈO VẠC - HÀ GIANG] 🌟\n\n❤️ Chỉ với 10,000 VNĐ, bạn có thể góp phần mang đến một cuộc sống tốt đẹp hơn cho các em học sinh vùng cao Mèo Vạc - Hà Giang. Quỹ Thiện Nguyện Sinh Viên kêu gọi sự đóng góp của các bạn để xây dựng nhà vệ sinh số 10 và số 11, giúp cải thiện điều kiện sinh hoạt và học tập của các em nhỏ nơi đây.\n\nTại sao lại cần xây nhà vệ sinh? Ở những vùng cao như xã Sùng Trà điều kiện sinh hoạt rất khó khăn, đặc biệt là việc thiếu thốn các nhà vệ sinh đạt chuẩn. Điều này không chỉ ảnh hưởng đến sức khỏe mà còn làm giảm chất lượng học tập của các em. Với mục tiêu mang lại một môi trường sống và học tập an toàn hơn, Quỹ Thiện Nguyện Sinh Viên hy vọng bạn sẽ cùng chung tay xây dựng hai nhà vệ sinh mới cho các em.\n\n💚 Quyền lợi khi tham gia quyên góp: Khi đóng góp, bạn sẽ nhận được giấy chứng nhận từ Quỹ Thiện Nguyện Sinh Viên. Đây cũng là cơ hội để bạn vừa giúp đỡ cộng đồng, vừa tích lũy thêm điểm số cho quá trình rèn luyện của mình.\n\n🧡 Quỹ Thiện Nguyện Sinh Viên tin rằng sự chung tay của mỗi người sẽ mang lại những thay đổi tích cực cho cuộc sống của các em nhỏ vùng cao. Hãy biến 10,000 VNĐ của bạn thành niềm hy vọng cho các em nhỏ Mèo Vạc!\n\n---------------------------------------------\n\n📜 Mọi thắc mắc xin vui lòng liên hệ:\n🎉Fanpage: Quỹ Thiện Nguyện Sinh Viên\n📞 SĐT: 0369.559.342 (Mai Nhung)\n📧Email: thiennguynsinhvien@gmail.com\n---------------------------------------------\n\nQUỸ THIỆN NGUYỆN SINH VIÊN\nĐỂ YÊU THƯƠNG KHÔNG CHỈ LÀ LỜI NÓI!"
        binding.description.setText(fullText)
        // Kiểm tra nếu nội dung quá dài thì mặc định chỉ hiển thị 3 dòng
        binding.description.post(Runnable {
            if (binding.description.getLineCount() > 4) {
                binding.description.setMaxLines(4)
                binding.description.setEllipsize(TextUtils.TruncateAt.END)
            }
        })
    }
    fun settingUpListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.description.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View) {
                if (isExpanded) {
                    // Nếu đang mở rộng, thu gọn văn bản lại
                    binding.description.setMaxLines(4)
                    binding.description.setEllipsize(TextUtils.TruncateAt.END)
                } else {
                    // Nếu đang thu gọn, mở rộng văn bản ra
                    binding.description.setMaxLines(Int.MAX_VALUE)
                    binding.description.setEllipsize(null)
                }
                isExpanded = !isExpanded
            }
        })
    }
}