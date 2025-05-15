def branch = env.BRANCH_NAME

echo "🔀 Dispatcher: Current branch is '${branch}'"

if (branch == 'main') {
    load 'main/Jenkinsfile'
} else if (branch == 'onprem') {
    load 'onprem/Jenkinsfile'
} else {
    error "❌ No Jenkinsfile defined for branch: ${branch}"
}