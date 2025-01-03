package com.example.wanderpedia.core.data.repository

import com.example.wanderpedia.core.data.error.AuthException.UserNotFoundException
import com.example.wanderpedia.core.data.source.remote.AccountService
import com.example.wanderpedia.core.domain.model.User
import com.example.wanderpedia.core.domain.model.usecase.DataResult
import com.example.wanderpedia.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UserRepositoryImplTest {

    private lateinit var accountService: AccountService

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        accountService = mock()
        userRepository = UserRepositoryImpl(accountService)
    }

    @Test
    fun `currentUser should return DataResult_Success when user is not null`() = runTest {
        val user = User("testUser")
        whenever(accountService.currentUser).thenReturn(flowOf(user))

        val result = userRepository.currentUser.first()

        assertIs<DataResult.Success<User>>(result)
        assertEquals(user, result.data)
    }

    @Test
    fun `currentUser should return DataResult_Error when user is null`() = runTest {
        whenever(accountService.currentUser).thenReturn(flowOf(null))

        val result = userRepository.currentUser.first()

        assertIs<DataResult.Error>(result)
        assertTrue(result.exception is UserNotFoundException)
    }

    @Test
    fun `createAnonymousAccount should return DataResult_Success when account creation is successful`() =
        runTest {
            whenever(accountService.createAnonymousAccount()).thenReturn(Unit)

            val result = userRepository.createAnonymousAccount()

            assertIs<DataResult.Success<Unit>>(result)
        }

    @Test
    fun `createAnonymousAccount should return DataResult_Error when account creation fails`() =
        runTest {
            val exception = RuntimeException("Account creation failed")
            whenever(accountService.createAnonymousAccount()).thenThrow(exception)

            val result = userRepository.createAnonymousAccount()

            assertIs<DataResult.Error>(result)
            assertEquals(exception, result.exception)
        }

    @Test
    fun `updateDisplayName should return DataResult_Success when update is successful`() = runTest {
        val newDisplayName = "NewDisplayName"
        whenever(accountService.updateDisplayName(newDisplayName)).thenReturn(Unit)

        val result = userRepository.updateDisplayName(newDisplayName)

        assertIs<DataResult.Success<Unit>>(result)
    }

    @Test
    fun `updateDisplayName should return DataResult_Error when update fails`() = runTest {
        val newDisplayName = "NewDisplayName"
        val exception = RuntimeException("Update failed")
        whenever(accountService.updateDisplayName(newDisplayName)).thenThrow(exception)

        val result = userRepository.updateDisplayName(newDisplayName)

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }

    @Test
    fun `linkAccountWithGoogle should return DataResult_Success when linking is successful`() =
        runTest {
            val idToken = "testIdToken"
            whenever(accountService.linkAccountWithGoogle(idToken)).thenReturn(Unit)

            val result = userRepository.linkAccountWithGoogle(idToken)

            assertIs<DataResult.Success<Unit>>(result)
        }

    @Test
    fun `linkAccountWithGoogle should return DataResult_Error when linking fails`() = runTest {
        val idToken = "testIdToken"
        val exception = RuntimeException("Linking failed")
        whenever(accountService.linkAccountWithGoogle(idToken)).thenThrow(exception)

        val result = userRepository.linkAccountWithGoogle(idToken)

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }

    @Test
    fun `linkAccountWithEmail should return DataResult_Success when linking is successful`() =
        runTest {
            val email = "test@example.com"
            val password = "password"
            whenever(accountService.linkAccountWithEmail(email, password)).thenReturn(Unit)

            val result = userRepository.linkAccountWithEmail(email, password)

            assertIs<DataResult.Success<Unit>>(result)
        }

    @Test
    fun `linkAccountWithEmail should return DataResult_Error when linking fails`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val exception = RuntimeException("Linking failed")
        whenever(accountService.linkAccountWithEmail(email, password)).thenThrow(exception)

        val result = userRepository.linkAccountWithEmail(email, password)

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }

    @Test
    fun `signInWithGoogle should return DataResult_Success when sign-in is successful`() = runTest {
        val idToken = "testIdToken"
        whenever(accountService.signInWithGoogle(idToken)).thenReturn(Unit)

        val result = userRepository.signInWithGoogle(idToken)

        assertIs<DataResult.Success<Unit>>(result)
    }

    @Test
    fun `signInWithGoogle should return DataResult_Error when sign-in fails`() = runTest {
        val idToken = "testIdToken"
        val exception = RuntimeException("Sign-in failed")
        whenever(accountService.signInWithGoogle(idToken)).thenThrow(exception)

        val result = userRepository.signInWithGoogle(idToken)

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }

    @Test
    fun `signInWithEmail should return DataResult_Success when sign-in is successful`() = runTest {
        val email = "test@example.com"
        val password = "password"
        whenever(accountService.signInWithEmail(email, password)).thenReturn(Unit)

        val result = userRepository.signInWithEmail(email, password)

        assertIs<DataResult.Success<Unit>>(result)
    }

    @Test
    fun `signInWithEmail should return DataResult_Error when sign-in fails`() = runTest {
        val email = "test@example.com"
        val password = "password"
        val exception = RuntimeException("Sign-in failed")
        whenever(accountService.signInWithEmail(email, password)).thenThrow(exception)

        val result = userRepository.signInWithEmail(email, password)

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }

    @Test
    fun `signOut should return DataResult_Success when sign-out is successful`() = runTest {
        whenever(accountService.signOut()).thenReturn(Unit)

        val result = userRepository.signOut()

        assertIs<DataResult.Success<Unit>>(result)
    }

    @Test
    fun `signOut should return DataResult_Error when sign-out fails`() = runTest {
        val exception = RuntimeException("Sign-out failed")
        whenever(accountService.signOut()).thenThrow(exception)

        val result = userRepository.signOut()

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }

    @Test
    fun `deleteAccount should return DataResult_Success when deletion is successful`() = runTest {
        whenever(accountService.deleteAccount()).thenReturn(Unit)

        val result = userRepository.deleteAccount()

        assertIs<DataResult.Success<Unit>>(result)
    }

    @Test
    fun `deleteAccount should return DataResult_Error when deletion fails`() = runTest {
        val exception = RuntimeException("Deletion failed")
        whenever(accountService.deleteAccount()).thenThrow(exception)

        val result = userRepository.deleteAccount()

        assertIs<DataResult.Error>(result)
        assertEquals(exception, result.exception)
    }
}