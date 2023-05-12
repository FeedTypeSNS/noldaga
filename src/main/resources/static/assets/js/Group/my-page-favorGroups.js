$(document).ready(function() {
    $.ajax({
        url: '/api/groups/member/favor',
        method: 'GET',
        success: function(response) {
            var groupList = response.result; // 받은 데이터에서 그룹 리스트 추출
            console.log(groupList);
            // 리스트 출력
            var ul = $('<ul>').addClass('list-inline mb-0 text-center text-sm-start mt-3 mt-sm-0');

            $.each(groupList, function(index, group) {
                var li = $('<li>').addClass('flex-shrink-0 avatar avatar-xs me-2');
                var a = $('<a>').attr('href', 'group?id='+group.id);
                var img
                if(group.profile_url == "" || group.profile_url == null) {
                    img = $('<img>').addClass('avatar-img rounded-circle').attr('src', '/assets/images/avatar/placeholder.jpg').attr('alt', '');
                } else {
                    img = $('<img>').addClass('avatar-img rounded-circle').attr('src', group.profile_url).attr('alt', '');
                }

                a.append(img);
                li.append(a);
                ul.append(li);
            });

            // 결과를 원하는 요소에 추가
            $('#favorGroups').append(ul); // 출력을 원하는 요소의 ID를 지정해야 합니다.
        }
    });
});