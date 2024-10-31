const data = [
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    {
        "id": 1,
        "title": "test1",
        "content": "test1",
        "files": [
            {
                "fileId": "66279a23-0804-4283-97f8-7df204355a99",
                "fileName": " (7).jpg",
                "fileUrl": "/DATA/upload/board/e249ab9f-6da9-46a1-912b-97cad17bec05_다운로드 (7).jpg"
            }
        ],
        "createdAt": "2024-10-13T18:16:40.526796",
        "readCount": 0,
        "username": "admin"
    },
    
    
];
window.onload = function() {

    const postContainer = document.querySelector('.card-con');
    
    // Create post element

    let c = '';
    data.forEach(d => {
        c += `
            <ul class="card-box">
                <li class="card-img">
                    <img src="/api/v1/board/file?boardId=${d.id}&fileId=${d.files[0]?.fileId}"
                        alt="게시물 이미지"
                        loading="lazy"
                        style="width:100%; height:100%; object-fit:cover;"/>
                </li>
                <li class="card-detail">
                    <span class="title">제목${d.title}</span>
                    <span class="content">내용${d.content}</span>
                    <span class="date">날짜${d.createdAt}</span>
                </li>
            </ul>
        `;
    })


    postContainer.innerHTML = c;

}


