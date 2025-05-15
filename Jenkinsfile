def branch = env.BRANCH_NAME
echo "🔀 Dispatcher: Current branch is '${branch}'"

node {
    // 현재 작업 디렉토리 출력
    sh 'pwd'

    // 현재 디렉토리의 파일 목록 출력
    sh 'ls -al'

    if (branch == 'main') {
        load 'main/Jenkinsfile'
    } else if (branch == 'onprem') {
        load 'onprem/Jenkinsfile'
    } else {
        error "❌ No Jenkinsfile defined for branch: ${branch}"
    }
}
