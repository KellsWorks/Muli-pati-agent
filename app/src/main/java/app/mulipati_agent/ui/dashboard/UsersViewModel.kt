package app.mulipati_agent.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.mulipati.util.Resource
import app.mulipati_agent.db.repositories.UsersRepository
import app.mulipati_agent.network.responses.users.AppUser

class UsersViewModel @ViewModelInject constructor(
    repository: UsersRepository
) : ViewModel() {

    val users: LiveData<Resource<List<AppUser>>> = repository.getUsers()

}