let addUserForm = $("#add-form")
let modalUserForm = $("#modal-form")
let inputs = $("div.user-form-inputs")

if (window.location.pathname === "/user") {
    $("#v-pills-profile").tab('show')
    $("#v-pills-tab a").addClass("active")
}


$('#userEditModal').on('show.bs.modal', function (event) {
    let button = $(event.relatedTarget)
    let action = button.data("action")
    let userId = button.data("userid")

    prepareForm(action, userId)

    getUser(userId)
        .then(user => modalUserForm.jsonToForm(user,{
            roles: function (value){
                let roles = value.map(role => role.role)
                $('[name="roles"]').val(roles);
            }
        }))
})

$('a[href="#profile"]').on('shown.bs.tab', function (e) {
    prepareForm("add")
})

async function getUser(id) {
    let url = "/api/v1/admin/users/" + id
    let response = await fetch(url)
    let user;
    if (response.ok) {
        user = await response.json()
        return user;
    } else {
        console.error("Ошибка HTTP: " + response.status)
        return null
    }
}

function prepareForm(action, userId = 0) {
    let idInput = $("#id")
    let firstNameInput = $("#firstName")
    let lastNameInput = $("#lastName")
    let ageInput = $("#age")
    let emailInput = $("#email")
    let passwordInput = $("#password")
    let rolesInput = $("#roles")

    if (action === 'add') {
        //id
        idInput.prop({type:"hidden", readonly: "true"})
        idInput.parent().addClass("d-none")
        //firstName
        firstNameInput.removeAttr("readonly")
        //lastName
        lastNameInput.removeAttr("readonly")
        //age
        ageInput.removeAttr("readonly")
        //email
        emailInput.removeAttr("readonly")
        //password
        passwordInput.parent().removeClass("d-none")
        passwordInput.removeAttr("readonly")
        //roles
        rolesInput.removeAttr("disabled")
        //append and reset
        inputs.prependTo(addUserForm)
        addUserForm[0].reset()
    } else if (action === "edit") {
        //id
        idInput.prop({type:"text", readonly: "true"})
        idInput.parent().removeClass("d-none")
        //firstName
        firstNameInput.removeAttr("readonly")
        //lastName
        lastNameInput.removeAttr("readonly")
        //age
        ageInput.removeAttr("readonly")
        //email
        emailInput.removeAttr("readonly")
        //password
        passwordInput.parent().removeClass("d-none")
        passwordInput.removeAttr("readonly")
        //roles
        rolesInput.removeAttr("disabled")
        //append and reset
        inputs.appendTo(modalUserForm)
        modalUserForm[0].reset()
        //change form action url
        modalUserForm.prop({action: "/admin/edit", method: "POST"})
        //change modal title and button
        $("#userEditModalLabel").text("Edit user")
        $("button.modal-submit-button")
            .text("Edit")
            .removeClass("btn-danger")
            .addClass("btn-primary")
    } else if (action === "delete") {
        //id
        idInput.prop({type:"text", readonly: "true"})
        idInput.parent().removeClass("d-none")
        //firstName
        firstNameInput.prop({readonly: "true"})
        //lastName
        lastNameInput.prop({readonly: "true"})
        //age
        ageInput.prop({readonly: "true"})
        //email
        emailInput.prop({readonly: "true"})
        //password
        passwordInput.parent().addClass("d-none")
        passwordInput.prop({readonly: "true"})
        //roles
        rolesInput.prop({disabled: "true"})
        //append and reset
        inputs.appendTo(modalUserForm)
        modalUserForm[0].reset()
        //change form action url
        modalUserForm.prop({action: "/admin/delete/"+userId, method: "GET"})
        //change modal title and button
        $("#userEditModalLabel").text("Delete user")
        $("button.modal-submit-button")
            .text("Delete")
            .removeClass("btn-primary")
            .addClass("btn-danger")
    }


}