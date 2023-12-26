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
    const koreanCheckbox = document.getElementById("Korean");
    const japaneseCheckbox = document.getElementById("Japanese");
    const chineseCheckbox = document.getElementById("Chinese");
    const westernCheckbox = document.getElementById("Western");
    const selectAllCheckbox = document.getElementById("All");

    const checkboxes = [koreanCheckbox, japaneseCheckbox, chineseCheckbox, westernCheckbox];

    // 각 카테고리 체크박스의 변화를 감지하여 '전체 선택하기' 상태를 조절합니다.
    checkboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            if (!checkbox.checked) {
                selectAllCheckbox.checked = false;
            } else {
                const allChecked = checkboxes.every(cb => cb.checked);
                selectAllCheckbox.checked = allChecked;
            }
        });
    });

    // '전체 선택하기' 체크박스의 상태에 따라 모든 카테고리 체크박스를 제어합니다.
    selectAllCheckbox.addEventListener('change', () => {
        checkboxes.forEach(checkbox => {
            checkbox.checked = selectAllCheckbox.checked;
        });
    });
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

                return response.json();
            })
            .then(data => {
                console.log('서버 응답:', data);

                if (data && data.recommendedRestaurants && data.recommendedRestaurants.length > 0) {
                    let listElement = $('ol.list-group');
                    listElement.empty();

                    data.recommendedRestaurants.forEach(function (item) {
                        listElement.append(`
                            <li class="list-group-item">
                                <div class="ms-2 me-auto">
                                    <div class="fw-bold">${item.category}</div>
                                    <div>${item.streetNameAddress}</div>
                                    <div>${item.restaurantName}</div>
                                </div>
                            </li>
                        `);
                    });
                } else {
                    // Handle case when no data or empty array received
                    $('.alert.alert-warning').show();
                    $('ol.list-group').empty();
                }
            })
            .catch(error => {
                console.error('Request failed:', error);
            });
    });
    function goToResultPage() {
        window.location.href = '페이지 URL'; // 페이지 URL로 수정
    }
}