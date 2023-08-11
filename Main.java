import java.util.*;

/*
1. 단어 후보 정렬
2. 가능한 단어 후보 리스트 생성
3. 재귀함수를 사용한 패스워드 생성 + 중복 연산을 방지하기 위해 메모이제이션(캐싱)
*/
public class Main {
	// 메모이제이션을 위한 배열 (캐싱)
	static String[] dp;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		// 패스워드 길이 N, 단어 후보 갯수 M 입력
		int N = scanner.nextInt();
		int M = scanner.nextInt();
		
		// 줄바꿈 후 패스워드 패턴 입력
		scanner.nextLine();
		String pwPattern = scanner.nextLine();
		
		// 단어 후보 저장하는 배열 pwWords 생성 후 패스워드를 구성할 단어 후보 입력
		String[] pwWords = new String[M];
		for (int i = 0; i < M; i++) {
		pwWords[i] = scanner.nextLine();
		}
		
		// 입력받은 단어 후보 배열 pwWords를 사전순 정렬
		Arrays.sort(pwWords);

		// 단어 후보의 인덱스 저장할 nextWords 배열 생성
		List<Integer>[] nextWords = new ArrayList[N];
		
		// 입력받은 패스워드 패턴 길이(N)만큼 반복
		for (int i = 0; i < N; i++) {
			// i 번째 인덱스 저장
			nextWords[i] = new ArrayList<>();
			// 입력받은 단어 후보(M)만큼 반복
			for (int j = 0; j < M; j++) {
				String pwWord = pwWords[j];
				// pwWord길이가 현재 위치에서 패스워드 끝까지의 길이보다 작거나 같고 isMatch 메소드를 호출하여 패스워드 패턴과 같다면 nextWords[i] 에 j번째 단어 추가
				if (pwWord.length() <= N - i && isMatch(pwPattern.substring(i, i + pwWord.length()), pwWord)) {
					nextWords[i].add(j);
				}
			}
		}
		
		// 메모이제이션 배열을 길이(N)으로 초기화
		dp = new String[N];
		// solve 메소드 호출하여 패스워드 생성
		String finalPassword = solve(pwPattern, 0, pwWords, nextWords);
		// 최종 결과가 null 일때 예외처리 또는 최종 출력
		if (finalPassword == null) {
			System.out.println("Impossible Partern");
		} else {
			System.out.println(finalPassword);
		}
	}

	/* 백트래킹 && 메모이제이션 재귀 함수
	password: 현재까지 생성된 패스워드, idx: 검증하는 현재 인덱스, pwWords: 단허 후보, nextWords: 각 위치에서 사용할 수 있는 단허 후보 인덱스 */
	static String solve(String password, int idx, String[] pwWords, List<Integer>[] nextWords) {
		// index 위치가 password 길이와 같으면 password 생성이 끝났음으로 리턴
		if (idx == password.length()) {
			return password;
		}

		// 메모이제이션 배열에 현재 검증하는 인덱스가 존재하는 경우, 즉 계산한 패스워드인 경우 리턴
		if (dp[idx] != null) {
			return dp[idx];
		}

		String result = null;
		// 현재 위치(인덱스)에서 사용 가능한 단어 후보를 반복하며 패스워드 생성
		for (int i : nextWords[idx]) {
			String word = pwWords[i];
			
			// 생성된 패스워드의 처음 부터 idx(현재 위치 인덱스)까지 + 새로운 단어 word + idx(현재 위치 인덱스) 이후의 패스워드를 합친 신규 패스워드 생성
			String newPassword = password.substring(0, idx) + word + password.substring(idx + word.length());
			
			// 신규 패스워드 + idx, pwWords, nextWords 로 solve 함수 호출 후 나머지 패스워드 생성하여 temp 변수에 저장
			String temp = solve(newPassword, idx + word.length(), pwWords, nextWords);
			
			// 유효한 패스워드 생성 실패(null)가 아니거나 result 가 null 또는 compareTo 메소드로 비교 시 사전 순으로 앞섰을 경우 result 를 temp로 업데이트하여 항상 result가 현재까지 찾은 패스워드 중 사전 순으로 가장 빠르게 조건 설정
			if (temp != null && (result == null || temp.compareTo(result) < 0)) {
				result = temp;
			}
		}
		// 최종적으로, idx(현재 위치)에서 사전순으로 가장 빠른 패스워드를 리턴
		return dp[idx] = result;
	}

	// 패스워드 패턴과 단어가 일치하는지 확인하는 메소드
	public static boolean isMatch(String pattern, String word) {
		// 패턴과 단어 길이 우선 검증
		if (pattern.length() != word.length()) {
			return false;
		}
		// 패스워드 패턴을 반복
		for (int i = 0; i < pattern.length(); i++) {
			// 패턴의 i번째가 '?'가 아니면서 문자가 동일하지 않으면 일치하지 않는 단어니 false 리턴 ('?'일 경우 어떤 문자와도 일치할 수 있다)
			if (pattern.charAt(i) != '?' && pattern.charAt(i) != word.charAt(i)) {
				return false;
			}
		}
		// 모든 문자가 동일하면 true 리턴
		return true;
	}

}
