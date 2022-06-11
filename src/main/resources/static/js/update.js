// (1) 회원정보 수정
function update(userId, event) {
	event.preventDefault(); //폼태그의 액션을 막아버림 막지않으면 form태그가 액션을 따라가는데 
							//이 함수를 호출한 form태그는 액션이 없으므로 자기자신을 호출한다. 그러면서 동작이 제대로 이루어지지않는다.
							//그렇기 때문에 update()함수가 제대로 작동할 수 있도록 preventDefault()를 통해 기본행위를 막아줘야한다.
	let data = $("#profileUpdate").serialize();
	
	console.log(data);
	
	$.ajax({
		type:"put",
		url:`/api/user/${userId}`,
		data:data,
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType:"json"
	}).done(res=>{ //httpStatus 상태코드 200
		console.log("성공",res);
		location.href=`/user/${userId}`
	}).fail(error=>{ //httpstatus 상태코드 200이 아닐경우
		console.log("실패",error.responseJSON.data);
		console.log(JSON.stringify(error.responseJSON.data));
		if(error.data == null) {
			alert(error.responseJSON.message);
		} else {
		alert(error.responseJSON.data.name);
		}
	});
}