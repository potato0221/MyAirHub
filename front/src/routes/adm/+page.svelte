<script lang="ts">
	import rq from '$lib/rq/rq.svelte';
	import type { components } from '$lib/types/api/v1/schema';

	let myData: components['schemas']['MemberDto'][] = [];

	const addAirport = async () => {
		const { data, error } = await rq.apiEndPoints().POST('/api/v1/airport/add');

		if (data) {
			rq.msgInfo('공항이 추가 되었습니다.');
		} else if (error) {
			rq.msgError('api 호출 오류');
		}
	};

	const updateLocation = async () => {
		const { data, error } = await rq.apiEndPoints().PUT('/api/v1/airport/update/location');

		if (data) {
			rq.msgInfo('위치 정보가 업데이트 되었습니다.');
		} else if (error) {
			rq.msgError('api 호출 오류');
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
				<p class="text-xl">관리자 페이지</p>
			</div>
			<div class="ml-4">
				<button
					on:click={addAirport}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>공항 추가</button
				>
			</div>
			<div class="ml-4">
				<button
					on:click={updateLocation}
					class="inline-block rounded-md border border-gray-400 bg-white px-2 py-1 text-sm font-medium font-semibold text-gray-800 shadow-sm hover:bg-gray-700 hover:text-white focus:outline-none"
					>위치 추가</button
				>
			</div>
		</div>
	</div>
{/await}
