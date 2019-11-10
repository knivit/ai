package com.tsoft.ai.simulatedannealing.engine;

import com.tsoft.ai.simulatedannealing.config.ApplicationProperties;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.function.Consumer;

@AllArgsConstructor
public class SimulatedAnnealingService {

    private MathService mathService;
    private ApplicationProperties applicationProperties;

    /**
     * Рассматривается алгоритм отжига как способ выполнения проце
     * дур поиска и оптимизации. Данный метод является аналогом процесса нагрева
     * ния тела до состояния плавления с последующим постепенным охлаждением. При
     * высоких температурах поиск ведется по всему диапазону. При снижении темпе
     * ратуры диапазон поиска уменьшается до небольшой области вокруг текущего ре
     * шения. Алгоритм проиллюстрирован с помощью классической задачи размеще
     * ния N ферзей на шахматной доске.
     */

    /**
     * Метод отжига может быть эффективным при решении задач различных клас
     * сов, требующих оптимизации. Ниже приводится их краткий список:
     * - создание пути;
     * - реконструкция изображения;
     * - назначение задач и планирование;
     * - размещение сети;
     * - глобальная маршрутизация;
     * - обнаружение и распознавание визуальных объектов;
     * - разработка специальных цифровых фильтров.
     * Поскольку метод отжига представляет собой процесс генерации случайных
     * чисел, поиск решения с использованием данного алгоритма может занять значи
     * тельное время. В некоторых случаях алгоритм вообще не находит решение или
     * выбирает не самое оптимальное.
     */
    public void findSolution(Consumer<SolutionStep> onSolutionStep) {
        MemberType current = initializeSolution();
        computeEnergy(current);

        MemberType best = new MemberType(applicationProperties.getMaxLength());
        best.setEnergy(Double.MAX_VALUE);
        boolean solutionFound = false;

        MemberType working = current.clone();

        int timer = 0;
        double temperature = applicationProperties.getInitialTemperature();
        while (temperature > applicationProperties.getFinalTemperature()) {
            // Кол-во худших, но принятых моделей (для более полного поиска)
            int worseAccepted = 0;

            /* Измененяем решение случайным образом */
            for (int step = 0; step < applicationProperties.getStepsPerChange(); step ++) {
                boolean foundBetter = false;
                boolean useWorse = false;

                tweakSolution(working);
                computeEnergy(working);

                if (working.getEnergy() <= current.getEnergy()) {
                    foundBetter = true;
                } else {
                    double test = Math.random();
                    double delta = working.getEnergy() - current.getEnergy();

                    // Вероятность допуска на основе закона термодинамики
                    // При высоких температурах симулированное восстановление позволяет принимать
                    // худшие решения для того, чтобы произвести более полный поиск решений
                    double probability = Math.exp(-delta / temperature);
                    if (probability > test) {
                        worseAccepted ++;
                        useWorse = true;
                    }
                }

                if (foundBetter || useWorse) {
                    current = working.clone();

                    if (current.getEnergy() < best.getEnergy()) {
                        best = current.clone();
                        solutionFound = true;
                    }
                } else {
                    working = current.clone();
                }
            }

            SolutionStep ss = new SolutionStep();
            ss.setTimer(timer ++);
            ss.setTemperature(temperature);
            ss.setEnergy(best.getEnergy());
            ss.setWorseAcceptedPercent((worseAccepted * 100.0) / applicationProperties.getStepsPerChange());
            onSolutionStep.accept(ss);

            System.out.println(String.format("%d. Temperature: %.8f, energy = %.8f", timer, temperature, best.getEnergy()));

            temperature *= applicationProperties.getAlpha();
        }

        if (solutionFound) {
            System.out.println(String.format("Found solution with energy (number of conflicts) = %.8f:", best.getEnergy()));
            emitSolution(best);
        } else {
            System.out.println("Solution not found");
        }
    }

    private void tweakSolution(MemberType memberType) {
        int x = mathService.getRandom(applicationProperties.getMaxLength());
        int y = mathService.getRandom(applicationProperties.getMaxLength(), x);
        memberType.swap(x, y);
    }

    private MemberType initializeSolution() {
        MemberType memberType = new MemberType(applicationProperties.getMaxLength());

        // Начальная инициализация решения
        for (int i = 0; i < applicationProperties.getMaxLength(); i ++) {
            memberType.set(i, i);
        }

        // Изменение решения случайным образом
        for (int i = 0; i < applicationProperties.getMaxLength(); i ++) {
            tweakSolution(memberType);
        }

        return memberType;
    }

    /* Замечание: по условию кодировки конфликты по вертикали
     * и горизонтали исключены
     */
    private void computeEnergy(MemberType memberType) {
        char[][] board = prepareBoard(memberType);

        int[] dx = new int[] {-1, 1, -1, 1};
        int[] dy = new int[] {-1, 1, 1, -1};
        int size = applicationProperties.getMaxLength();

        int conflicts = 0;

        for (int x = 0; x < size; x ++) {
            int y = memberType.get(x);

            /* Проверяем диагонали */
            for (int j = 0; j < 4; j ++) {
                int tempx = x;
                int tempy = y;

                while (true) {
                    tempx += dx[j];
                    tempy += dy[j];
                    if ((tempx < 0) || (tempx >= size) ||
                            (tempy < 0) || (tempy >= size)) break;
                    if (board[tempx][tempy] == 'Q') conflicts++;
                }
            }
        }

        memberType.setEnergy(conflicts);
    }

    private char[][] prepareBoard(MemberType memberType) {
        int size = applicationProperties.getMaxLength();

        char[][] result = new char[size][size];

        for (int i = 0; i < size; i ++) {
            Arrays.fill(result[i], ' ');
            result[i][memberType.get(i)] = 'Q';
        }
        return result;
    }

    private void emitSolution(MemberType memberType) {
        char[][] board = prepareBoard(memberType);

        int size = applicationProperties.getMaxLength();

        for (int y = 0; y < size; y ++) {
            for (int x = 0; x < size; x ++) {
                System.out.print(board[x][y] == 'Q' ? " Q" : " .");
            }
            System.out.println();
        }
    }
}
