
document.addEventListener('DOMContentLoaded', function () {
    window.addEventListener('scroll', function () {
        const header = document.querySelector('header');
        if (window.scrollY > 500) {
            header.style.backgroundColor = 'rgba(0,0,0,0.8)'; // 500px 이상일 때 흰색
        } else {
            header.style.backgroundColor = 'rgba(255,255,255,0.2)'; // 500px 이하일 때 검정색
        }
    });
});

