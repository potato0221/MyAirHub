<script lang="ts">
	import '$lib/app.css';
	import { page } from '$app/stores';
	import { untrack } from 'svelte';
	import rq from '$lib/rq/rq.svelte';

	let isMypage = $state(false);
	let isWeather = $state(false);
	let isAlarm = $state(true);
	let isState = $state(false);
	let isFlight = $state(false);

	const { children } = $props();
	rq.effect(async () => {
		untrack(() => {
			rq.initAuth();
		});
		isMypage = $page.url.pathname.includes('/member') ? true : false;
		isWeather = $page.url.pathname.includes('/weather') ? true : false;
		isState = $page.url.pathname.includes('/state') ? true : false;
		isFlight = $page.url.pathname.includes('/flight') ? true : false;
	});
</script>

<header class="navbar top-0 w-full bg-gray-50 shadow">
	<div class="flex-1">
		<div class="flex-none">
			<div class="dropdown">
				<div tabindex="0" role="button" class="btn btn-ghost btn-circle">
					<svg
						xmlns="http://www.w3.org/2000/svg"
						class="h-5 w-5"
						fill="none"
						viewBox="0 0 24 24"
						stroke="currentColor"
						><path
							stroke-linecap="round"
							stroke-linejoin="round"
							stroke-width="2"
							d="M4 6h16M4 12h16M4 18h7"
						/></svg
					>
				</div>
				<ul
					tabindex="0"
					class="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow"
				>
					{#if rq.isAdmin()}
						<li>
							<a href="/adm" class="font-semi-bold"
								><i class="fa-solid fa-right-to-bracket"></i> 관리자</a
							>
						</li>
					{/if}
					{#if rq.isLogout()}
						<li>
							<a class="font-semi-bold" href="/member/login"
								><i class="fa-solid fa-right-to-bracket"></i> 로그인</a
							>
						</li>
					{/if}
					{#if rq.isLogin()}
						{#if !rq.isAdmin()}
							<li>
								<a class="font-semi-bold" href="/qna"
									><i class="fa-regular fa-circle-question"></i> 1대1 문의</a
								>
							</li>
						{/if}
						<li class="font-semi-bold">
							<button on:click={() => rq.logout()}>
								<i class="fa-solid fa-right-from-bracket"></i> 로그아웃
							</button>
						</li>
					{/if}
				</ul>
			</div>
		</div>
		<div class="flex-1"></div>
	</div>
	<div class="flex flex-1 justify-center">
		<a href="/" class="font-bold">MyAirHub</a>
	</div>

	<div class="mr-4 flex-1 justify-end">
		<div class="relative flex items-center gap-x-4">
			<svg
				xmlns="http://www.w3.org/2000/svg"
				fill="none"
				viewBox="0 0 24 24"
				stroke-width="1.5"
				stroke="currentColor"
				class="h-6 w-6"
			>
				<path
					stroke-linecap="round"
					stroke-linejoin="round"
					d="M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"
				/>
			</svg>
		</div>
	</div>
</header>

<main class="mt-12">{@render children()}</main>

<footer class=" bottom-0 w-full bg-white text-gray-300">
	<div class="container mx-auto flex items-center justify-around">
		<div class="flex flex-1 flex-col items-center">
			{#if rq.isLogin()}
				<a href="/state">
					<div class="flex items-center justify-center">
						<div
							class={isState
								? 'flex h-9 w-9 items-center justify-center text-xl text-blue-600'
								: 'flex h-9 w-9 items-center justify-center text-xl text-gray-300'}
						>
							<i class="fa-solid fa-plane-departure text-2xl"></i>
						</div>
					</div>
					<p class={isState ? 'text-blue-600' : 'text-gray-300'}>항공기 위치</p>
				</a>
			{:else}
				<a href="/member/login">
					<div class="flex items-center justify-center">
						<div class="flex h-9 w-9 items-center justify-center text-xl">
							<i class="fa-solid fa-plane-departure text-2xl"></i>
						</div>
					</div>
					<p>항공기 위치</p>
				</a>
			{/if}
		</div>

		<div class="flex flex-1 flex-col items-center">
			{#if rq.isLogin()}
				<a href="/flight">
					<div class="flex items-center justify-center">
						<div
							class={isFlight
								? 'flex h-9 w-9 items-center justify-center text-xl text-blue-600'
								: 'flex h-9 w-9 items-center justify-center text-xl text-gray-300'}
						>
							<i class="fa-solid fa-ticket text-3xl"></i>
						</div>
					</div>
					<p class={isFlight ? 'text-blue-600' : 'text-gray-300'}>항공편 관리</p>
				</a>
			{:else}
				<a href="/member/login">
					<div class="flex items-center justify-center">
						<div class="flex h-9 w-9 items-center justify-center text-xl">
							<i class="fa-solid fa-ticket text-3xl"></i>
						</div>
					</div>
					<p>항공편 관리</p>
				</a>
			{/if}
		</div>
		<div class="flex flex-1 flex-col items-center">
			{#if rq.isLogin()}
				<a href="/weather">
					<div class="flex items-center justify-center">
						<div
							class={isWeather
								? 'flex h-9 w-9 items-center justify-center text-xl text-blue-600'
								: 'flex h-9 w-9 items-center justify-center text-xl text-gray-300'}
						>
							<i class="fa-solid fa-sun text-3xl"></i>
						</div>
					</div>
					<p class={isWeather ? 'text-blue-600' : 'text-gray-300'}>도착지 날씨</p>
				</a>
			{:else}
				<a href="/member/login">
					<div class="flex items-center justify-center">
						<div class="flex h-9 w-9 items-center justify-center text-xl">
							<i class="fa-regular fa-circle-user text-3xl"></i>
						</div>
					</div>
					<p>도착지 날씨</p>
				</a>
			{/if}
		</div>
		{#if rq.isAdmin()}
			<div class="flex flex-1 flex-col items-center">
				<a href="/adm">
					<div class="flex items-center justify-center">
						<div class="flex h-9 w-9 items-center justify-center text-xl">
							<i class="fa-regular fa-circle-user ml-1 text-3xl"></i>
						</div>
					</div>
					<p>관리자</p>
				</a>
			</div>
		{:else}
			<div class="flex flex-1 flex-col items-center">
				{#if rq.isLogin()}
					<a href="/member/me">
						<div class="flex items-center justify-center">
							<div
								class={isMypage
									? 'flex h-9 w-9 items-center justify-center text-xl text-blue-600'
									: 'flex h-9 w-9 items-center justify-center text-xl text-gray-300'}
							>
								<i class="fa-regular fa-circle-user text-3xl"></i>
							</div>
						</div>
						<p class={isMypage ? 'text-blue-600' : 'text-gray-300'}>마이 페이지</p>
					</a>
				{:else}
					<a href="/member/login">
						<div class="flex items-center justify-center">
							<div class="flex h-9 w-9 items-center justify-center text-xl">
								<i class="fa-regular fa-circle-user text-3xl"></i>
							</div>
						</div>
						<p>마이 페이지</p>
					</a>
				{/if}
			</div>
		{/if}
	</div>
</footer>

<style>
	.app {
		display: flex;
		flex-direction: column;
		min-height: 100vh;
	}

	main {
		flex: 1;
		display: flex;
		flex-direction: column;
		padding: 1rem;
		width: 100%;
		max-width: 64rem;
		margin: 0 auto;
		box-sizing: border-box;
	}

	footer {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 12px;
	}

	footer a {
		font-weight: bold;
	}

	@media (min-width: 480px) {
		footer {
			padding: 12px 0;
		}
	}
</style>
