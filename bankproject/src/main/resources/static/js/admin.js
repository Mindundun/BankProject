window.onload = function() {

    const postContainer = document.querySelector('.card-con');

    fetch('/api/v1/board', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(res => res.json())
    .then(res => {
        const data = res.data;
        console.log(data);
        let c = '';
        data.forEach(d => {
            c += `
                <ul class="card-box">
                    <li class="card-img" style="background:url('/api/v1/board/file?filePath=${d.files[0]?.fileUrl}') no-repeat center/cover;}"></li>
                    <li class="card-detail">
                        <span class="title">제목${d.title}</span>
                        <span class="content">내용${d.content}</span>
                        <span class="date">날짜${d.createdAt}</span>
                    </li>
                </ul>
            `;
        })
        postContainer.innerHTML = c;
    })
    .catch(err => console.error(err));


}


