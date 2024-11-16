<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let myData: components['schemas']['MemberDto'][] = [];

	let modalNickname;
	let newNickname = $state();

	function openModalNickname() {
		modalNickname.showModal();
	}

	function handleOutsideClickNickname(event) {
		if (event.target === modalNickname) {
			modalNickname.close();
		}
	}

	const updateNickname = async () => {
		console.log('Updating nickname with:', newNickname);
		const { data, error } = await rq.apiEndPoints().PUT('/api/v1/members/modifyNickName', {
			body: {
				nickname: newNickname
			}
		});

		if (data) {
			rq.msgInfo('닉네임이 변경 되었습니다.');
			modalNickname.close();
		} else {
			rq.msgError('이미 사용중인 닉네임 입니다.');
		}
	};

	async function load() {
		const { data } = await rq.apiEndPoints().GET('/api/v1/members/me');

		myData = data?.data.item ?? [];

		return { myData };
	}
</script>

{#await load()}
	<h1>loading...</h1>
{:then { myData }}
	<div class="mx-auto w-full max-w-lg px-4 sm:px-6 lg:px-8">
		<div class="mx-auto flex flex max-w-4xl">
			<div>
				<p class="text-xl">{myData.nickname}님 비행 정보</p>
			</div>
			<div class="ml-4">
				<button
					on:click={openModalNickname}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>닉네임 변경</button
				>
				<dialog
					id="my_modal_3"
					class="modal"
					bind:this={modalNickname}
					on:click={handleOutsideClickNickname}
				>
					<div class="modal-box">
						<form method="dialog">
							<button class="btn btn-circle btn-ghost btn-sm absolute right-2 top-2">✕</button>
						</form>
						<div class="m-4 flex flex-col rounded-lg bg-white">
							<label class="mb-2 text-lg font-semibold">닉네임 변경</label>
							<input
								type="text"
								bind:value={newNickname}
								id="newNickname"
								class="border-input bg-background placeholder:text-muted-foreground flex h-10 w-full rounded-md border px-3 py-2 text-sm focus:border-gray-700 focus:outline-none disabled:cursor-not-allowed disabled:opacity-50"
								placeholder="새 닉네임 입력"
							/>
							<div class="m-4 flex justify-end">
								<button
									on:click={updateNickname}
									class=" inline-block w-[100px] rounded-md border border-gray-400 bg-white px-4 py-2 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
									>변경하기</button
								>
							</div>
						</div>
					</div>
				</dialog>
			</div>
		</div>
	</div>
{/await}
