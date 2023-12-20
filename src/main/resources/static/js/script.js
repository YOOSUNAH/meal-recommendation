document.addEventListener('DOMContentLoaded', function () {
    const swiper = new Swiper('.swiper-container', {
        direction: 'horizontal',
        loop: false,

        slidesPerView: 1,
        spaceBetween: 10, // Optional: Set space between slides to 0
        centeredSlides: true,

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
        const {latitude, longitude} = position.coords;

        console.log(position);

        // HTML에서 체크된 카테고리를 가져와서 categoryList 배열에 추가
        const categoryList = [];
        if (document.getElementById("Korean").checked) {
            categoryList.push("KOREAN");
        }
        if (document.getElementById("Japanese").checked) {
            categoryList.push("JAPANESE");
        }
        if (document.getElementById("Chinese").checked) {
            categoryList.push("CHINESE");
        }
        if (document.getElementById("Western").checked) {
            categoryList.push("WESTERN");
        }

        // 요청할 JSON 데이터
        const requestData = {
            categoryList: categoryList,
            coordinates: {
                longitude: longitude,
                latitude: latitude,
            }
        };

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

                console.log('Received Response:', response);

                if (response.status === 304) {
                    return Promise.resolve();
                }

                return response.text();
            })
            .then(data => {
                console.log('서버 응답:', data);

                if (!data) {
                    console.error('Empty response received.');
                    return;
                }

                try {
                    const jsonData = JSON.parse(data);
                    console.log('Parsed JSON Data:', jsonData);
                } catch (error) {
                    console.error('Invalid JSON format:', error);
                }
            })
            .catch(error => {
                console.error('Request failed:', error);
            });
    });
}