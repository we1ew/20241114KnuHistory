$(function() {
    $("#logout_button").click(function(e) {
        $.ajax({
            method: "POST",
            url: "/Movie/ajax/logout.do",
            dataType: "json",
            success: function(data) {
                if(data.result) {
                    location.href = "/Movie/main.do";
                } else {
                    alert("올바르지 않은 요청입니다!");
                }
            },
            error: function(err) {
                console.log("movieMain error : ", err);
            }
        });
    });

    $.ajax({
        method: "POST",
        url: "/Movie/ajax/movieList.do",
        dataType: "json",
        success: function(data) {
            if(data.list.length > 0) {
                const select = $("#movie-title");
                for(item of data.list) {
                    html = "<option value='" + item.movie_id + "'>" + item.movie_nm + "</option>"
                    select.append(html);
                }
            }
        },
        error: function(err) {
            console.log("movieMain Init error : ", err);
        }
    });

    $("#rating_add_button").click(function(e) {
        const logined = $("#user_id").val() !== "";
        const movie_id = $("#movie-title").val();
        const rating = $("#rating").val();
        if(!logined) {
            alert("먼저 로그인을 해주세요")
        } else if(movie_id === "") {
            alert("영화를 선택해주세요");
        } else if(rating === "") {
            alert("점수를 선택해주세요");
        } else {
            $.ajax({
                method: "POST",
                url: "/Movie/ajax/addRating.do",
                data: {
                    movie_id: movie_id,
                    rating: rating
                },
                dataType: "json",
                success: function(data) {
                    if(data.result == 1) {
                        alert("영화 평가를 등록했습니다.");
                    } else if(data.result == 2) {
                        alert("영화 평가를 변경했습니다.");
                    }
                },
                error: function(err) {
                    console.log("movieMainAddRating Error : ", err);
                }
            });
        }
    });

    const getRating = function() {
        $.ajax({
          method: "POST",
          url: "/Movie/ajax/getRating.do",
          dataType: "json",
          success: function(data) {
              if(data.result.length > 0) {
                  const ratingList = $("#rating-list");
                  ratingList.empty();
                  for(item of data.result) {
                      htmlStr = "<tr>";
                      htmlStr += "<td>" + item.movie_id + "</td>";
                      htmlStr += "<td>" + item.movie_nm + "</td>";
                      htmlStr += "<td>" + item.director + "</td>";
                      htmlStr += "<td>" + (item.rating == '-' ? '등록된 평가가 없습니다' : item.rating) + "</td>";
                      htmlStr += "</tr>";
                      ratingList.append(htmlStr);
                  }
              }
          },
          error: function(err) {
              console.log("movieMainGetRating Error : ", err);
          }
      });
    }

    setInterval(getRating, 2000);

    getRating();
})