// 跳转未登录用户
if (!document.cookie.includes("isLoggedIn=true")) {
    window.location.href = "login.html";
}

function showToast(message) {
    const toast = document.getElementById("toast");
    toast.innerText = message;
    toast.style.display = "block";
    setTimeout(() => (toast.style.display = "none"), 2000);
}

function setupLoginRequiredButton(buttonId, actionName) {
    const btn = document.getElementById(buttonId);
    if (!btn) return;
    btn.addEventListener("click", function () {
        showToast(`${actionName}功能开发中...`);

        // 可选：更新按钮状态（如“拼团中...”）
        if (buttonId.startsWith("joinGroup")) {
            this.textContent = "拼团中...";
            this.style.background = "#cccccc";
            this.disabled = true;
            setTimeout(() => {
                this.textContent = "拼团成功";
                this.style.background = "#07c160";
            }, 2000);
        }
    });
}

// ✅ 只绑定非支付相关按钮
[
    { id: "joinGroup1", action: "加入拼团1" },
    { id: "joinGroup2", action: "加入拼团2" },
    { id: "collectBtn", action: "收藏" },
    { id: "cartBtn", action: "加入购物车" },
    { id: "serviceBtn", action: "联系客服" }
].forEach((btn) => setupLoginRequiredButton(btn.id, btn.action));

// 轮播图
document.addEventListener("DOMContentLoaded", function () {
    const wrapper = document.querySelector(".swiper-wrapper");
    const slides = document.querySelectorAll(".swiper-slide");
    const pagination = document.querySelector(".swiper-pagination");
    let index = 0;

    slides.forEach((_, i) => {
        const dot = document.createElement("div");
        dot.className = "swiper-pagination-bullet";
        if (i === 0) dot.classList.add("swiper-pagination-bullet-active");
        pagination.appendChild(dot);
    });

    const dots = document.querySelectorAll(".swiper-pagination-bullet");

    function updateSlider() {
        wrapper.style.transform = `translateX(-${index * 100}%)`;
        dots.forEach((b, i) => b.classList.toggle("swiper-pagination-bullet-active", i === index));
    }

    setInterval(() => {
        index = (index + 1) % slides.length;
        updateSlider();
    }, 3000);
});

// 倒计时
window.onload = () => {
    const elements = [document.getElementById("countdown1"), document.getElementById("countdown2")];
    let seconds = 349;
    function tick() {
        if (seconds <= 0) {
            elements.forEach(el => {
                el.textContent = "拼团已结束";
                el.style.background = "#e0e0e0";
                el.style.color = "#999";
            });
            return;
        }
        const min = Math.floor(seconds / 60).toString().padStart(2, "0");
        const sec = (seconds % 60).toString().padStart(2, "0");
        elements.forEach(el => el.textContent = `00:${min}:${sec}`);
        seconds--;
        setTimeout(tick, 1000);
    }
    tick();
};

// 显示支付弹窗
function showPaymentModal() {
    document.getElementById('payOverlay').style.display = 'block';
    document.getElementById('paymentModal').style.display = 'block';
}

// 隐藏支付弹窗
function hidePaymentModal() {
    document.getElementById('payOverlay').style.display = 'none';
    document.getElementById('paymentModal').style.display = 'none';
}

// 支付按钮绑定
document.getElementById('cancelPayBtn').addEventListener('click', hidePaymentModal);
document.getElementById('confirmPayBtn').addEventListener('click', () => {
    hidePaymentModal();
    showToast('支付成功！');
});
