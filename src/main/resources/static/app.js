if (window.location.pathname === "/user") {
    $("#v-pills-profile").tab('show')
    $("#v-pills-tab a").addClass("active")
    $('head title').text('User information-page')
}

populateCurrentUserInfo().then(r => console.log("OK"))

async function populateCurrentUserInfo() {
    let userInfoTable = $("table.user-info-table")
    let userEmailHeader = $("span.current-user-email")
    let userRolesHeader = $("span.current-user-roles")
    let currentUser = await getCurrentUser()
    let row = `<tr>
                    <td>${currentUser['id']}</td>
                    <td>${currentUser['firstName']}</td>
                    <td>${currentUser['lastName']}</td>
                    <td>${currentUser['age']}</td>
                    <td>${currentUser['email']}</td>
                    <td>${currentUser['rolesString']}</td>
                </tr>`
    userInfoTable.find('tbody').append(row)
    userEmailHeader.text(currentUser['email'])
    userRolesHeader.text(currentUser['rolesString'])
}

async function getCurrentUser() {
    let url = "/api/v1/user/current"
    let response = await fetch(url, {
        cache: "reload"
    })
    let user
    if (response.ok) {
        user = await response.json()
        return user;
    } else {
        console.error("Ошибка HTTP: " + response.status)
        return null
    }
}