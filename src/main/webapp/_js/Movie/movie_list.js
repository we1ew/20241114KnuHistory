$(function() {
    $(".filter-list").empty();

    $.ajax({
        method: "POST",
        url: "/Movie/ajax/genreList.do",
        data: {foo: "bar"},
        dataType: "json",
        success: function(data) {
            if(data.list) {
                for(item of data.list) {
                    let html = '<label><input type="checkbox" name="genre" value="' + item.genre_id + '"> ' + item.genre_nm + '</label>';
                    $(".filter-list").append(html);
                }
            }
        },
        error: function(err) {
            console.log("movieList error : ", err);
        }
    });

    $(".movie-grid").empty();

    $.ajax({
        method: "POST",
        url: "/Movie/ajax/movieList.do",
        data: {foo: "bar"},
        dataType: "json",
        success: function(data) {
            if(data.list) {
                for(item of data.list) {
                    let html = '<div class="movie-card">';
                    html += '<img src="' + item.poster + '" alt="영화 포스터">';
                    html += '<p><strong>감독:</strong> ' + item.director + '</p>';
                    html += '<p><strong>장르:</strong> ' + item.genre + '</p>';
                    html += '<button>평점 보기</button>';
                    html += '</div>';
                    $(".movie-grid").append(html);
                }
            }
        },
        error: function(err) {
            console.log("movieList error : ", err);
        }
    });

    $(".apply-button").click(function(e) {
        const checkedList = $("input[name='genre']:checked");

        if(checkedList.length > 0) {
            const checkedValue = checkedList.map(function() {
                                                     return this.value;
                                                 }).get().join(',');

            $(".movie-grid").empty();

            $.ajax({
                method: "POST",
                url: "/Movie/ajax/movieList.do",
                data: {filter: checkedValue},
                dataType: "json",
                success: function(data) {
                    if(data.list) {
                        for(item of data.list) {
                            let html = '<div class="movie-card">';
                            html += '<img src="' + item.poster + '" alt="영화 포스터">';
                            html += '<p><strong>감독:</strong> ' + item.director + '</p>';
                            html += '<p><strong>장르:</strong> ' + item.genre + '</p>';
                            html += '<button>평점 보기</button>';
                            html += '</div>';
                            $(".movie-grid").append(html);
                        }
                    }
                },
                error: function(err) {
                    console.log("movieList error : ", err);
                }
            });
        } else {
            alert('선택된 장르가 없습니다!!');
        }
    });
})