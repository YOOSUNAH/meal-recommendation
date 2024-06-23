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
        const categoryList = [];

        // 체크된 카테고리 가져오기
        const koreanCheckbox = document.getElementById("Korean");
        const japaneseCheckbox = document.getElementById("Japanese");
        const chineseCheckbox = document.getElementById("Chinese");
        const westernCheckbox = document.getElementById("Western");

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
                if (data && data.recommendedRestaurants && data.recommendedRestaurants.length > 0) {
                    const listElement = document.querySelector('ol.list-group');
                    listElement.innerHTML = ''; // 리스트 초기화

                    data.recommendedRestaurants.forEach(function (item) {
                        const listItem = document.createElement('li');
                        listItem.classList.add('list-group-item');
                        listItem.innerHTML = `
                        <div class="ms-2 me-auto">
                            <div class="fw-bold">${item.category}</div>
                            <div>${item.streetNameAddress}</div>
                            <div>${item.restaurantName}</div>
                        </div>
                    `;
                        listElement.appendChild(listItem);
                    });
                } else {
                    const alertWarning = document.querySelector('.alert.alert-warning');
                    if (alertWarning) {
                        alertWarning.style.display = 'block';
                    }
                    const listGroup = document.querySelector('ol.list-group');
                    if (listGroup) {
                        listGroup.innerHTML = '';
                    }
                }
            })
            .catch(error => {
                console.error('Request failed:', error);
            });
    });
}