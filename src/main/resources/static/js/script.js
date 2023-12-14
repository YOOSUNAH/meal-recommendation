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

function recommend() {

    navigator.geolocation.getCurrentPosition(position => {
        // lat = position.coords.latitude;
        // long = position.coords.longitude;
        const { latitude, longitude } = position.coords;

        console.log(position);

        // 요청할 JSON 데이터
        // var requestData = {
        const requestData = {
            categoryList: [
                "KOREAN",
                "JAPANESE",
            ],
            coordinates: {
                longitude: longitude,
                latitude: latitude,
            }
        };

        // JSON 데이터를 문자열로 변환
        // var jsonData = JSON.stringify(requestData);
        //
        // // XMLHttpRequest 객체 생성
        // var xhr = new XMLHttpRequest();
        //
        // // POST 요청 초기화
        // xhr.open('POST', '/api/recommend', true);
        //
        // // 요청 헤더 설정 (JSON 형식으로 전송함을 명시)
        // xhr.setRequestHeader('Content-type', 'application/json');
        //
        // // 요청 완료 시 실행되는 함수 설정
        // xhr.onreadystatechange = function () {
        //     if (xhr.readyState === XMLHttpRequest.DONE) {
        //         // 요청이 완료되고 응답을 받았을 때 실행할 코드
        //         if (xhr.status === 200) {
        //             // 요청 성공
        //             var response = JSON.parse(xhr.responseText);
        //             console.log('서버 응답:', response);
        //         } else {
        //             // 요청 실패
        //             console.log('요청 실패:', xhr.status);
        //         }
        //     }
        // };
        // // JSON 데이터 전송
        // xhr.send(jsonData);


        // fetch API를 사용하여 POST 요청 보내기
        fetch('/api/recommend', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('서버 응답:', data);
            })
            .catch(error => {
                console.error('요청 실패:', error);
            });

    })
}