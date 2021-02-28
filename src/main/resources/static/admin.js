let addUserForm = $("#add-form")
let modalUserForm = $("#modal-form")
let inputs = $("div.user-form-inputs")
let allUsersTable = $("table.all-users")

populateAllUsersTable().then(r => console.log("OK"))

let addUserSubmit = (e) => {
    e.preventDefault()
    let body = formToJSON(e.target)

    console.log(body)

    fetch('/api/v1/admin/users', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: body
    })
        .then(response => response.json())
        .then((user) => {
            console.log("user add ok: " + user.id)
            addUserForm[0].reset()
            $('#myTab a[href="#home"]').tab('show')
            allUsersTable.find('tbody').children('tr').remove()
            populateAllUsersTable().then(r => console.log("table filled"))
        })
}
addUserForm[0].addEventListener('submit', addUserSubmit)

function formToJSON(elem) {
    let output = {};
    new FormData(elem).forEach(
        (value, key) => {
            // Check if property already exist
            if (Object.prototype.hasOwnProperty.call(output, key)) {
                let current = output[key];
                if (!Array.isArray(current)) {
                    // If it's not an array, convert it to an array.
                    current = output[key] = [current];
                }
                current.push(value); // Add the new value to the array.
            } else {
                output[key] = value;
            }
        }
    );
    return JSON.stringify(output);
}

async function populateAllUsersTable() {
    let users = await getAllUsers();

    let rows = users
        .map(user => {
            return `<tr>
                        <td>${user['id']}</td>
                        <td>${user['firstName']}</td>
                        <td>${user['lastName']}</td>
                        <td>${user['age']}</td>
                        <td>${user['email']}</td>
                        <td>${user['rolesString']}</td>
                        <td>
                            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#userEditModal" data-userid="${user['id']}"
                                    data-action="edit">
                                Edit
                            </button>
                        </td>
                        <td>
                            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#userEditModal"
                                    data-userid="${user['id']}" data-action="delete">
                                Delete
                            </button>
                        </td>
                    </tr>`
        })
        .join("")

    allUsersTable.find('tbody').append(rows)
}


async function getAllUsers() {
    let url = "/api/v1/admin/users"
    let response = await fetch(url, {
        cache: "reload"
    })
    let users
    if (response.ok) {
        users = await response.json()
        return users;
    } else {
        console.error("Ошибка HTTP: " + response.status)
        return null
    }
}

let editUserSubmit = (e) => {
    e.preventDefault()
    console.log("edit", e.target);

    let body = formToJSON(e.target)
    let userId = $("#id").val()

    console.log(body, userId)


    fetch('/api/v1/admin/users/edit/'+userId, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: body
    })
        .then(response => response.json())
        .then((user) => {
            console.log("user edit ok: " + user.id)
            modalUserForm[0].reset()
            $('#userEditModal').modal('hide')
            allUsersTable.find('tbody').children('tr').remove()
            populateAllUsersTable().then(r => console.log("table filled"))
        })
}

let deleteUserSubmit = (e) => {
    e.preventDefault()
    console.log("delete", e.target);

    let body = formToJSON(e.target)
    let userId = $("#id").val()

    console.log(body, userId)

    fetch('/api/v1/admin/users/delete/'+userId, {
        method: 'DELETE',
        body: body
    })
        .then(() => {
            console.log("user delete ok")
            modalUserForm[0].reset()
            $('#userEditModal').modal('hide')
            allUsersTable.find('tbody').children('tr').remove()
            populateAllUsersTable().then(r => console.log("table filled"))
        })
}

$('#userEditModal').on('show.bs.modal', async function (event) {
    let button = $(event.relatedTarget)
    let action = button.data("action")
    let userId = button.data("userid")

    await prepareForm(action, userId)

    getUser(userId)
        .then(user => modalUserForm.jsonToForm(user, {
            roles: function (value) {
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

async function prepareForm(action, userId = 0) {
    let idInput = $("#id")
    let firstNameInput = $("#firstName")
    let lastNameInput = $("#lastName")
    let ageInput = $("#age")
    let emailInput = $("#email")
    let passwordInput = $("#password")
    let rolesInput = $("#roles")

    populateRolesSelect().then(r => console.log("get roles ok"))

    if (action === 'add') {
        //id
        idInput.prop({type: "hidden", readonly: "true"})
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
        idInput.prop({type: "text", readonly: "true"})
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
        //add event listener
        modalUserForm[0].removeEventListener('submit', deleteUserSubmit)
        modalUserForm[0].addEventListener('submit', editUserSubmit)
        //change modal title and button
        $("#userEditModalLabel").text("Edit user")
        $("button.modal-submit-button")
            .text("Edit")
            .removeClass("btn-danger")
            .addClass("btn-primary")
    } else if (action === "delete") {
        //id
        idInput.prop({type: "text", readonly: "true"})
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
        //add event listener
        modalUserForm[0].removeEventListener('submit', editUserSubmit)
        modalUserForm[0].addEventListener('submit', deleteUserSubmit)
        //change modal title and button
        $("#userEditModalLabel").text("Delete user")
        $("button.modal-submit-button")
            .text("Delete")
            .removeClass("btn-primary")
            .addClass("btn-danger")
    }
}

async function populateRolesSelect() {
    let allRoles = await getAllRoles()
    let rolesInput = $("#roles")

    let options = allRoles
        .map(role => {
            return `<option value="${role['role']}">${role['roleName']}</option>`
        })
        .join("")

    rolesInput.children('option').remove()
    rolesInput.append(options)

}

async function getAllRoles() {
    let url = "/api/v1/admin/roles"
    let response = await fetch(url, {
        cache: "reload"
    })
    let roles
    if (response.ok) {
        roles = await response.json()
        return roles;
    } else {
        console.error("Ошибка HTTP: " + response.status)
        return null
    }
}