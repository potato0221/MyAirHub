<script>
	import rq from '$lib/rq/rq.svelte';

	let modal;
	let username = '';
	let password = '';

	function openModal() {
		modal.showModal();
	}

	const login = async (event) => {
		event.preventDefault();
		const { data, error } = await rq.apiEndPointsWithFetch(fetch).POST('/api/v1/members/login', {
			body: {
				username: username,
				password: password
			}
		});

		if (data) {
			rq.msgInfo(data.msg);
			rq.goTo('/');
			location.reload();
		}
	};
</script>

<div class="flex flex-col items-center justify-center gap-20">
	<h1 class="mt-8 text-5xl font-bold">My Air Hub</h1>
	<div class="space-y-4">
		<div class="flex justify-center">
			<a href={rq.getKakaoLoginUrl()}>
				<img src="/kakao_login_medium_narrow.png" alt="" />
			</a>
		</div>
		<div class="flex justify-center">
			<a href={rq.getGoogleLoginUrl()}>
				<img src="/web_light_sq_ctn@1x.png" alt="" />
			</a>
		</div>
		<div class="flex justify-center">
			<!-- 기존 onclick을 최신 on:click 구문으로 변경 -->
			<button
				class="ring-offset-background focus-visible:ring-ring disabled:opacity-50h-10 flex w-[190px] items-center justify-center whitespace-nowrap rounded-md bg-white px-4 py-3 text-sm font-medium text-black shadow-md transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:pointer-events-none"
				on:click={openModal}>관리자 로그인</button
			>
		</div>
		<dialog id="my_modal_3" class="modal" bind:this={modal}>
			<div class="modal-box">
				<form on:submit={login} class="flex flex-col p-6">
					<!-- 폼에 on:submit 이벤트 추가 -->
					<button
						type="button"
						class="btn btn-sm btn-circle btn-ghost absolute right-2 top-2"
						on:click={() => modal.close()}>✕</button
					>
					<label for="username">아이디</label>
					<input type="text" placeholder="username" class="mb-2 w-2/3" bind:value={username} />
					<label for="password">비밀번호</label>
					<input type="password" placeholder="password" class="max-w-xs" bind:value={password} />
					<div class="flex justify-center">
						<button
							type="submit"
							class="ring-offset-background focus-visible:ring-ring inline-flex h-10 items-center justify-center whitespace-nowrap rounded-md px-4 py-2 text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50"
							>관리자 로그인</button
						>
					</div>
				</form>
			</div>
		</dialog>
	</div>
</div>
