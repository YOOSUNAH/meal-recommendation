document.addEventListener('DOMContentLoaded', function () {
    const swiper = new Swiper('.swiper-container', {
        // Optional parameters
        direction: 'horizontal',
        loop: false,

        // // Set slides per view to 1
        slidesPerView: 1,
        spaceBetween: 10, // Optional: Set space between slides to 0
        centeredSlides: true,

        // Enable mousewheel control
        // mousewheel: {
        //     invert: false,
        // },

        // If we need pagination
        pagination: {
            el: '.swiper-pagination',
            clickable: true
        },

        // Navigation arrows
        navigation: {
            nextEl: '.swiper-button-next-custom',
            prevEl: '.swiper-button-prev',
        },

        // And if we need scrollbar
        scrollbar: {
            el: '.swiper-scrollbar',
            hide: true,
        },
    });
});

function selectAllCategory() {
    // '전체 선택하기' 체크박스의 상태를 가져옵니다.
    let selectAll = document.getElementById("All").checked;

    // 모든 체크박스의 상태를 '전체 선택하기' 체크박스의 상태와 동일하게 만듭니다.
    document.getElementById("Korean").checked = selectAll;
    document.getElementById("Japanese").checked = selectAll;
    document.getElementById("Chinese").checked = selectAll;
    document.getElementById("Western").checked = selectAll;
}